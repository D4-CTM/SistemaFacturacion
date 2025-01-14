CREATE TABLE recetas (
    id_ingrediente INT,
    id_plato INT,
    cantidad_necesaria DECIMAL(10, 2) NOT NULL CHECK (cantidad_necesaria > 0), --Cantidad de los ingredientes necesarios
    PRIMARY KEY (id_ingrediente, id_plato),
    FOREIGN KEY (id_ingrediente) REFERENCES ingredientes(id),
    FOREIGN KEY (id_plato) REFERENCES platos(id)
);