package com.facturacion.backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RestaurantItems {
    public static enum Items {
        RecipeIngredient,
        Ingredient,
        Plate
    }

    public static class RecipeIngredient {
        public int plate_id = -1;
        public int ingredient_id = -1;
        public float ingredient_needed;
    
        public RecipeIngredient(float _ingredient_needed, int _plate_id, int _ingredient_id) {
            ingredient_needed = _ingredient_needed;
            ingredient_id = _ingredient_id;
            plate_id = _plate_id;
        }

        public void insertRecipeIngredient(Connection connection) {
            String sqlStatement = "INSERT INTO recetas(id_plato, id_ingrediente, cantidad_necesaria) VALUES(?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)) {
                preparedStatement.setInt(1, plate_id);
                preparedStatement.setInt(2, ingredient_id);
                preparedStatement.setDouble(3, ingredient_needed);

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void deleteRecipeIngredient(Connection connection) {
            String sqlStatement = "DELETE FROM recetas WHERE id_plato = ? AND id_ingrediente = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)) {
                preparedStatement.setInt(1, plate_id);
                preparedStatement.setInt(2, ingredient_id);

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return "plate id: " + plate_id + "\ningredient id: " + ingredient_id + " \ningredients needed: " + ingredient_needed;
        }

    }
    
    public static class Plate {
        public int frequency;
        public float price;
        public String name;
        public int id = -1;
    
        public Plate(String _name, float _price, int _frequency, int _id) {
            frequency = _frequency;
            price = _price;
            name = _name;
            id = _id;
        }

        public Plate(String _name) {
            name = _name;
            frequency = 0;
        }

        public boolean isValid() {
            return !name.isBlank() && name.length() <= 255;
        }

        public void updateFrequency() { 
            frequency++;
        }

        public void changeName(String _name) {
            name = _name;
        }

        public void updatePlate(Connection connection) {
            if (id == -1)  return ;
            String sqlStatement = "UPDATE platos SET nombre = ?, frecuencia = ?, precio = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)) {
                preparedStatement.setString(1, name.toUpperCase());
                preparedStatement.setInt(2, frequency);
                preparedStatement.setFloat(3, price);
                preparedStatement.setInt(4, id);

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void deletePlate(Connection connection) {
            if (id == -1) return ;
            String sqlStatement = "DELETE FROM platos WHERE id = ?; DELETE FROM recetas WHERE id_plato = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)) {
                preparedStatement.setInt(1, id);
                preparedStatement.setInt(2, id);

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void insertPlate(Connection connection) {
            String sqlStatement = "INSERT INTO platos(nombre, frecuencia, precio) VALUES(?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)) {
                preparedStatement.setString(1, name.toUpperCase());
                preparedStatement.setInt(2, frequency);
                preparedStatement.setFloat(3, price);

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {

                    try (ResultSet key = preparedStatement.getGeneratedKeys()) {
                        while (key.next()) {
                            id = key.getInt(1);
                        }
                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return "id: " + id + "\tname: " + name + "\tfrequency: " + frequency;
        }

    }
    
    public static class Ingredient {
        public float quantity;
        public String name;
        public String unit;
        public int id = -1;
    
        public Ingredient(String _name, String _unit, float _quantity, int _id) {
            quantity = _quantity;
            name = _name;
            unit = _unit;
            id = _id;
        }

        public Ingredient(String _name, String _unit, float _quantity) {
            quantity = _quantity;
            unit = _unit;
            name = _name;
        }

        public void modifyData(String _name, String _unit, float _quantity) {
            quantity = _quantity;
            unit = _unit;
            name = _name;
        }

        public void updateIngredient(Connection connection) {
            if (id == -1) return ;
            String sqlStatement = "UPDATE ingredientes SET nombre = ?, unidad = ?, cantidad = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)) {
                preparedStatement.setString(1, name.toUpperCase());
                preparedStatement.setString(2, unit.toLowerCase());
                preparedStatement.setFloat(3, quantity);
                preparedStatement.setInt(4, id);

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void deleteIngredient(Connection connection) {
            if (id == -1) return ;
            String sqlStatement = "DELETE FROM ingredientes WHERE id = ?; DELETE FROM recetas WHERE id_ingredientes = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)) {
                preparedStatement.setInt(1, id);
                preparedStatement.setInt(2, id);

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void insertIngredient(Connection connection) {
            String sqlStatement = "INSERT INTO ingredientes(nombre, unidad, cantidad) VALUES(?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)) {
                preparedStatement.setString(1, name.toUpperCase());
                preparedStatement.setString(2, unit.toLowerCase());
                preparedStatement.setFloat(3, quantity);
                
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {

                    try (ResultSet key = preparedStatement.getGeneratedKeys()) {
                        while (key.next()) {
                            id = key.getInt(1);
                        }
                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public boolean isValid() {
            return !name.isBlank() && quantity >= 0;
        }

        @Override
        public String toString() {
            return "id: " + id + " name: " + name + " quantity: " + quantity + " " + unit;
        }

    }
}