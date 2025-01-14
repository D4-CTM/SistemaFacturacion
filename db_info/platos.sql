CREATE TABLE platos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL UNIQUE,
    precio DECIMAL(10, 2) NOT NULL CHECK(precio > 0)
    frecuencia INTEGER NOT NULL CHECK(frecuencia >= 0)
);