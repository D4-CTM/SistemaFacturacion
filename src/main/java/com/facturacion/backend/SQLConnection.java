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
    private int offset = 0;

    public SQLConnection(final String _username, final String _password) {
        hikari = new HikariDataSource();
        hikari.setJdbcUrl("jdbc:postgresql://localhost/restaurante");
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

    public LinkedList<Ingredient> getIngredientsAt(int page) {
        offset = page * OPP;
        return getCurrentIngredients();
    }

    public LinkedList<Ingredient> getCurrentIngredients() {
        String sql = "SELECT * FROM ingredientes WHERE id > 0 OFFSET ? LIMIT ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                preparedStatement.setInt(1, offset);
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
            connection.setAutoCommit(false);

            if (element instanceof Plate plate) {
                
                return plate.insertPlate(connection);

            } else if (element instanceof Ingredient ingredient) {

                return ingredient.insertIngredient(connection);

            } else if (element instanceof RecipeIngredient recipeIngredient) {
                recipeIngredient.insertRecipeIngredient(connection);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    
    public boolean deleteElement(Object element) {
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
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
            connection.setAutoCommit(false);
            if (element instanceof Plate plate) {
                return plate.updatePlate(connection);
            } else if (element instanceof Ingredient ingredient) {
                return ingredient.updateIngredient(connection);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


}
