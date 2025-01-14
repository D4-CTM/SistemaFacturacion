CREATE TABLE ingredientes (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL UNIQUE,
    cantidad DECIMAL(10, 2) NOT NULL CHECK (cantidad >= 0),
    unidad VARCHAR(5) NOT NULL DEFAULT 'lb'
);