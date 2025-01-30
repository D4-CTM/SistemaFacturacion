package com.facturacion.backend;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import com.facturacion.backend.RestaurantItems.*;
import com.zaxxer.hikari.HikariDataSource;

public class SQLConnection {
    private final HikariDataSource hikari;
    private final int OPP = 10; //OBJECTS PER PAGE
    private int ingredientOffset = 0;
    private int plateOffset = 0;

    public SQLConnection(final String _username, final String _password) {
        hikari = new HikariDataSource();
        hikari.setJdbcUrl("jdbc:postgresql://localhost:5440/restaurante");
        hikari.setUsername(_username);
        hikari.setPassword(_password);
    }

    private Connection getConnection() throws SQLException {
        return hikari.getConnection();
    }

    public int getPageCount(Items item) {
        return getItemCount(item)/OPP;
    }

    public int getItemCount(Items item) {
        String sql = switch (item) {
            case Ingredient -> "SELECT COUNT(id) FROM ingredientes;";
            case Plate -> "SELECT COUNT(id) FROM platos";
            default -> "";
        };
        if (sql.isBlank()) return -1;

        try (Connection connection = getConnection(); 
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
                resultSet.next();
                return resultSet.getInt("count");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public LinkedList<RecipeIngredient> fetchRecipeIngredients(Plate plate) {
        String sql = "SELECT id_ingrediente, cantidad_necesaria FROM recetas WHERE id_plato = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, plate.id);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) return null;
                    LinkedList<RecipeIngredient> recipeList = new LinkedList<>();
                    int ingredientId;
                    float quantityNeeded;
                    do {
                        ingredientId = resultSet.getInt("id_ingrediente");
                        quantityNeeded = resultSet.getFloat("cantidad_necesaria");
                        recipeList.add(new RecipeIngredient(quantityNeeded, plate.id, ingredientId));

                    } while (resultSet.next());
                    return recipeList;
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updatePlateTable(Plate plate, LinkedList<RecipeIngredient> recipeList) {
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            if (!plate.updatePlate(connection)) {
                return false;
            }

            if (recipeActions(recipeList, plate.id, connection)) {
                connection.commit();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean insertPlate(Plate plate, LinkedList<RecipeIngredient> recipeList) {
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            if (!plate.insertPlate(connection)) {
                return false;
            }
            
            if (recipeActions(recipeList, plate.id, connection)) {
                connection.commit();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        return false;
    }

    private boolean recipeActions(LinkedList<RecipeIngredient> recipeList, int id, Connection connection) {
        if (recipeList == null || recipeList.isEmpty()) {
            return true;
        }
        
        for (final RecipeIngredient recipeIngredient : recipeList) {
            if (!fetchIdOf(recipeIngredient)) return false;

            recipeIngredient.plate_id = id;

            if (!recipeIngredient.takeAction(connection)) return false;
        }

        return true;
    }

    public boolean fetchIdOf(RecipeIngredient recipe) {
        String sql = "SELECT id FROM ingredientes WHERE nombre = \'" + recipe.ingredient_name + "\';";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
                if (!resultSet.next()) return false;
                recipe.ingredient_id = resultSet.getInt("id");
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String getNameOf(int elementId, Items item) {
        if (elementId < 0) return "";
        String sql = switch (item) {
            case Ingredient -> {
                yield "SELECT nombre FROM ingredientes WHERE id = ?";
            }

            case Plate -> {
                yield "SELECT nombre FROM platos WHERE id = ?";
            }

            default -> {
                throw new IllegalArgumentException("Recetas no cuenta con algun campo de id en particular");
            }
        };

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            preparedStatement.setInt(1, elementId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) return "";
                return resultSet.getString("nombre");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "";
    }

    public String getUnityOf(String ingredientName) {
        String sql = "SELECT unidad FROM ingredientes WHERE nombre = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
             statement.setString(1, ingredientName);
             try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) return "";
                return resultSet.getString("unidad");
             }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public LinkedList<String> getIngredientsNames() {
        String sql = "SELECT nombre FROM ingredientes WHERE id > 0 ORDER BY id;";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)){
                if (!resultSet.next()) return null;
                LinkedList<String> list = new LinkedList<>();

                do {
                    list.add(resultSet.getString("nombre"));
                } while (resultSet.next());

                return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } 

        return null;
    }

    public LinkedList<Ingredient> getIngredientsAt(int page) {
        ingredientOffset = page * OPP;
        return getCurrentIngredients();
    }

    public LinkedList<Ingredient> getCurrentIngredients() {
        String sql = "SELECT * FROM ingredientes WHERE id > 0 ORDER BY id OFFSET ? LIMIT ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                preparedStatement.setInt(1, ingredientOffset);
                preparedStatement.setInt(2, OPP);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    LinkedList<Ingredient> list = new LinkedList<>();
                    if (!resultSet.next()) return list;
                    do {
                        list.add((Ingredient) identifyItem(Items.Ingredient, resultSet));
                    } while (resultSet.next());
                    return list;
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LinkedList<Plate> getPlatesAt(int page) {
        plateOffset = page * OPP;
        return getCurrentPlates();
    }

    public LinkedList<Plate> getCurrentPlates() {
        String sql = "SELECT * FROM platos WHERE id > 0 ORDER BY id OFFSET ? LIMIT ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                preparedStatement.setInt(1, plateOffset);
                preparedStatement.setInt(2, OPP);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    LinkedList<Plate> list = new LinkedList<>();
                    if (!resultSet.next()) return list;
                    do {
                        list.add((Plate) identifyItem(Items.Plate, resultSet));
                    } while (resultSet.next());
                    return list;
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object identifyItem(Items item, ResultSet resultSet) throws SQLException{
        return switch (item) {
            case Ingredient -> {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("nombre");
                String unit = resultSet.getString("unidad");
                float quantity = resultSet.getFloat("cantidad");

                yield new Ingredient(name, unit, quantity, id);
            }

            case Plate -> {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("nombre");
                float price = resultSet.getFloat("precio");
                int frequency = resultSet.getInt("frecuencia");

                yield new Plate(name, price, frequency, id);
            }

            default -> null;
        };

    }

    public Object fetch(int elementId, Items item) {
        if (elementId < 0) return null;
        String sqlString = switch (item) {
            case Ingredient -> {                
                yield "SELECT * FROM ingredientes WHERE id = ?";
            }

            case Plate -> {
                yield "SELECT * FROM platos WHERE id = ?";
            }

            default -> {
                throw new IllegalArgumentException("Recetas no cuenta con algun campo de nombre");
            }
        };


        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            preparedStatement.setInt(1, elementId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return identifyItem(item, resultSet);
                    }
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Object fetch(String elementName, Items item) {
        if (elementName.isBlank()) return null;
        String sqlString = switch (item) {
            case Ingredient -> {
                yield "SELECT * FROM ingredientes WHERE nombre = ?";
            }

            case Plate -> {
                yield "SELECT * FROM platos WHERE nombre = ?";
            }

            default -> {
                throw new IllegalArgumentException("Recetas no cuenta con algun campo de nombre");
            }
        };

        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                preparedStatement.setString(1, elementName);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return identifyItem(item, resultSet);
                    }
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean insertElement(Object element) {
        try (Connection connection = getConnection()) {

            if (element instanceof Plate plate) {
                
                return plate.insertPlate(connection);

            } else if (element instanceof Ingredient ingredient) {

                return ingredient.insertIngredient(connection);

            } else if (element instanceof RecipeIngredient recipeIngredient) {

                return recipeIngredient.insertRecipeIngredient(connection);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    
    public boolean deleteElement(Object element) {
        try (Connection connection = getConnection()) {
            if (element instanceof Plate plate) {
                return plate.deletePlate(connection);
            } else if (element instanceof Ingredient ingredient) {
                return ingredient.deleteIngredient(connection);
            } else if (element instanceof RecipeIngredient recipeIngredient) {
                return recipeIngredient.deleteRecipeIngredient(connection);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean modifyElement(Object element) {
        try (Connection connection = getConnection()) {

            if (element instanceof Plate plate) {
                return plate.updatePlate(connection);
            } else if (element instanceof Ingredient ingredient) {
                return ingredient.updateIngredient(connection);
            } else if (element instanceof RecipeIngredient recipeIngredient) {
                return recipeIngredient.updateRecipeIngredient(connection);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


}
