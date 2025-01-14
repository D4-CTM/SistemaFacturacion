package com.facturacion.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.facturacion.backend.RestaurantItems.*;

public class SQLConnection {
    private final String username;
    private final String password;
    
    public SQLConnection(final String _username, final String _password) {
        username = _username;
        password = _password;

        Object element = fetch("Tajadas", Items.Ingredient);
        if (element instanceof Ingredient ingredient) {
            System.out.println(ingredient.toString());
        }
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost/restaurante", username, password);
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


        try (Connection connection = createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString)) {
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

        try (Connection connection = createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString)) {
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

    public void insertElement(Object element) {
        try (Connection connection = createConnection()) {

            if (element instanceof Plate plate) {
                plate.insertPlate(connection);
            } else if (element instanceof Ingredient ingredient) {
                ingredient.insertIngredient(connection);
            } else if (element instanceof RecipeIngredient recipeIngredient) {
                recipeIngredient.insertRecipeIngredient(connection);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void deleteElement(Object element) {
        try (Connection connection = createConnection()) {

            if (element instanceof Plate plate) {
                plate.deletePlate(connection);
            } else if (element instanceof Ingredient ingredient) {
                ingredient.deleteIngredient(connection);
            } else if (element instanceof RecipeIngredient recipeIngredient) {
                recipeIngredient.deleteRecipeIngredient(connection);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modifyElement(Object element) {
        try (Connection connection = createConnection()) {

            if (element instanceof Plate plate) {
                plate.updatePlate(connection);
            } else if (element instanceof Ingredient ingredient) {
                ingredient.updateIngredient(connection);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
