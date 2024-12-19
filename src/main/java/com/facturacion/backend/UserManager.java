package com.facturacion.backend;

public class UserManager {
    
    


    class User {
        public String username;
        public String password;
        public boolean admin;

        public User(String _username, String _password, boolean _admin) {
            username = _username;
            password = _password;
            admin = _admin;
        }
    }
}
