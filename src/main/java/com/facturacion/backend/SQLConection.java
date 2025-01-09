package com.facturacion.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConection {

    public SQLConection() {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/restaurante", "joush", "Delcids4312")) {

            System.out.println("connection succesfully");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
}
