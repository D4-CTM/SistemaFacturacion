package com.facturacion.backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Hashtable;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

public class UserManager {
    private final Path userFilePath = Path.of("./.Reportes/Usuarios.yaml");
    private final String superUser = "Sudo";
    private final Yaml yaml;
    private User currentUser;

    public UserManager() throws IOException, UserManagerException {
        File dataDir = new File(userFilePath.getParent().toString());
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        File userFile = userFilePath.toFile();

        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setIndent(4);
        dumperOptions.setPrettyFlow(true);
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        Representer representer = new Representer(dumperOptions);
        representer.addClassTag(User.class, org.yaml.snakeyaml.nodes.Tag.MAP);

        yaml = new Yaml(representer, dumperOptions);

        if (!userFile.exists()) {
            userFile.createNewFile();
            initializeYaml();
        }
        currentUser = null;
    }

    private void initializeYaml() throws IOException, UserManagerException {
        addUser(superUser, "Delcids4312", true);
    }

    public void addUser(String username, String password, boolean admin) throws FileNotFoundException, IOException, UserManagerException {
        try (FileReader fileReader = new FileReader(userFilePath.toString())) {
            Map<String, Object> map = yaml.load(fileReader);
            if (map == null) {
                map = new Hashtable<>();
            }
            if (map.containsKey(username)) {
                throw new UserManagerException("¡El nombre de usuario debe ser unico!");
            }

            map.put(username, new User(username, hashString(password), admin));
            try (FileWriter fileWriter = new FileWriter(userFilePath.toString())) {
                yaml.dump(map, fileWriter);
            }
        }
    }

    public User getUser(String username, String password) throws FileNotFoundException, IOException, UserManagerException {
        try (FileReader fileReader = new FileReader(userFilePath.toFile())) {
            Map<String, Map<String, Object>> map = yaml.load(fileReader);
            if (map == null || !map.containsKey(username)) throw new UserManagerException("¡El nombre de usuario no existe!");

            long pass = hashString(password);
            Map<String, Object> userData = map.get(username);
            if (pass != (long) userData.get("password")) throw new UserManagerException("¡La contraseña ingresada es incorrecta!");

            User user = new User(
                (String) userData.get("username"),
                (long) userData.get("password"),
                (boolean) userData.get("admin")
            );

            return user;
        }        
    }

    private final long hashString(final String text) {
        long hash = 1;

        for (final char c : text.toCharArray()) {
            hash = (c * hash)^c;
        }

        return hash;
    }

    public void removeUser(String username, String password) throws FileNotFoundException, IOException, UserManagerException {
        if (username == superUser) throw new UserManagerException("¡No se puede eliminar el super usuario!");
        try (FileReader fileReader = new FileReader(userFilePath.toString())) {
            Map<String, Object> map = yaml.load(fileReader);
            if (map == null) {
                throw new UserManagerException("¡No se han ingresado usuarios!");
            }

            if (!map.containsKey(username)) {
                throw new UserManagerException("¡No existe usuario con dicho nombre!");
            }

            map.remove(username);
            try (FileWriter fileWriter = new FileWriter(userFilePath.toString())) {
                yaml.dump(map, fileWriter);
            }
        }
    }

    public void logIn(String username, String password) throws FileNotFoundException, IOException, UserManagerException {
        currentUser = getUser(username, password);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLogedIn() {
        return currentUser != null;
    }

    class User {
        public String username;
        public long password;
        public boolean admin;

        public User(final String _username, final long _password, final boolean _admin) {
            username = _username;
            password = _password;
            admin = _admin;
        }
    }

    public class UserManagerException extends Exception {
        public UserManagerException(final String _message) { super(_message); }
    }
}
