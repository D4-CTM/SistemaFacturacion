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

    public static enum Actions {
        Insert,
        Update,
        Delete
    }

    public static class RecipeIngredient {
        public int plate_id = -1;
        public int ingredient_id = -1;
        public float ingredient_needed;
        public String ingredient_name;
        public Actions action;

        public RecipeIngredient(float _ingredient_needed, int _plate_id, int _ingredient_id) {
            ingredient_needed = _ingredient_needed;
            ingredient_id = _ingredient_id;
            plate_id = _plate_id;
            action = Actions.Insert;
        }

        public RecipeIngredient(float _ingredient_needed, String _ingredient_name, Actions _action) {
            ingredient_needed = _ingredient_needed;
            ingredient_name = _ingredient_name;
            action = _action;
        }

        public RecipeIngredient(float _ingredient_needed, String _ingredient_name) {
            ingredient_needed = _ingredient_needed;
            ingredient_name = _ingredient_name;
            action = Actions.Insert;
        }

        public boolean takeAction(Connection connection) {
            switch (action) {
                case Insert: return insertRecipeIngredient(connection);
                case Update: return updateRecipeIngredient(connection);
                case Delete: return deleteRecipeIngredient(connection);
                default: return false;
            }
        }

        public boolean insertRecipeIngredient(Connection connection) {
            String sqlStatement = "INSERT INTO recetas(id_plato, id_ingrediente, cantidad_necesaria) VALUES(?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)) {
                preparedStatement.setInt(1, plate_id);
                preparedStatement.setInt(2, ingredient_id);
                preparedStatement.setDouble(3, ingredient_needed);

                return preparedStatement.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

        public boolean deleteRecipeIngredient(Connection connection) {
            String sqlStatement = "DELETE FROM recetas WHERE id_plato = ? AND id_ingrediente = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)) {
                preparedStatement.setInt(1, plate_id);
                preparedStatement.setInt(2, ingredient_id);

                return preparedStatement.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

        public boolean updateRecipeIngredient(Connection connection) {
            String sql = "UPDATE recetas SET cantidad_necesaria = ? WHERE id_ingrediente = ? AND id_plato = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setFloat(1, ingredient_needed);
                preparedStatement.setInt(2, ingredient_id);
                preparedStatement.setInt(3, plate_id);

                return preparedStatement.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        public String toString() {
            return "plate_id: " + plate_id + "\tingrediente_id: " + ingredient_id + "\tingredient_name: " + ingredient_name + "\tingredient_needed: " + ingredient_needed;
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

        public Plate(String _name, float _price) {
            price = _price;
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

        public boolean updatePlate(Connection connection) {
            if (id == -1)  return false;
            String sqlStatement = "UPDATE platos SET nombre = ?, frecuencia = ?, precio = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)) {
                preparedStatement.setString(1, name);
                preparedStatement.setInt(2, frequency);
                preparedStatement.setFloat(3, price);
                preparedStatement.setInt(4, id);

                return preparedStatement.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return false;
        }

        public boolean deletePlate(Connection connection) {
            if (id == -1) return false;
            String sqlStatement = "DELETE FROM platos WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)) {
                preparedStatement.setInt(1, id);

               return preparedStatement.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

        public boolean insertPlate(Connection connection) {
            String sqlStatement = "INSERT INTO platos(nombre, frecuencia, precio) VALUES(?, ?, ?) RETURNING id";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)) {
                preparedStatement.setString(1, name);
                preparedStatement.setInt(2, frequency);
                preparedStatement.setFloat(3, price);

                try (ResultSet key = preparedStatement.executeQuery()) {
                    if (key.next()) {
                        id = key.getInt(1);
                        return true;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return false;
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

        public boolean updateIngredient(Connection connection) {
            if (id == -1) return false;
            String sqlStatement = "UPDATE ingredientes SET nombre = ?, unidad = ?, cantidad = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, unit);
                preparedStatement.setFloat(3, quantity);
                preparedStatement.setInt(4, id);

                return preparedStatement.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return false;
        }

        public boolean deleteIngredient(Connection connection) {
            if (id == -1) return false;
            String sqlStatement = "DELETE FROM ingredientes WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)) {
                preparedStatement.setInt(1, id);

                return preparedStatement.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

        public boolean insertIngredient(Connection connection) {
            String sqlStatement = "INSERT INTO ingredientes(nombre, unidad, cantidad) VALUES(?, ?, ?) RETURNING id";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, unit);
                preparedStatement.setFloat(3, quantity);
                
                try (ResultSet key = preparedStatement.executeQuery()) {
                    if (key.next()) {
                        id = key.getInt(1);
                        return true;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

        public boolean isValid() {
            return !name.isBlank() && quantity >= 0;
        }

    }
}