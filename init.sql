CREATE TABLE ingredientes (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL UNIQUE,
    cantidad DECIMAL(10, 2) NOT NULL CHECK (cantidad >= 0),
    unidad VARCHAR(5) NOT NULL DEFAULT 'lb'
);

CREATE TABLE platos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL UNIQUE,
    precio DECIMAL(10, 2) NOT NULL CHECK(precio > 0),
    frecuencia INTEGER NOT NULL CHECK(frecuencia >= 0)
);

CREATE TABLE recetas (
    id_ingrediente INT,
    id_plato INT,
    cantidad_necesaria DECIMAL(10, 2) NOT NULL CHECK (cantidad_necesaria > 0), --Cantidad de los ingredientes necesarios
    PRIMARY KEY (id_ingrediente, id_plato),
    FOREIGN KEY (id_ingrediente) REFERENCES ingredientes(id) ON DELETE CASCADE,
    FOREIGN KEY (id_plato) REFERENCES platos(id) ON DELETE CASCADE
);
