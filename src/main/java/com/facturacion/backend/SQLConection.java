package com.facturacion.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConection {
    private final String url;
    private final String username;
    private final String password;

    public SQLConection() {
        url = "jdbc:postgresql://localhost:5432/restaurante";
        username = "joush";
        password = "Delcids4312";

        try {
            Class.forName("org.postgresql.Driver");

            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Conection done with success!");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public class Recipe {
        int plate_id;
        int ingredient_id;
        float ingredient_needed;

        public Recipe(int _plate_id, int _ingredient_id, float _ingredient_needed) {
            ingredient_needed = _ingredient_needed;
            ingredient_id = _ingredient_id;
            plate_id = _plate_id;
        }

    }

    public class Plate {
        public String name;
        public int id;

        public Plate(String _name, int _id) {
            name = _name;
            id = _id;
        }
    }

    public class Ingredient {
        public float quantity;
        public String name;
        public int id;

        public Ingredient(String _name, float _quantity, int _id) {
            quantity = _quantity;
            name = _name;
            id = _id;
        }
    }

}
