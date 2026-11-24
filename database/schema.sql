CREATE DATABASE IF NOT EXISTS peliculas_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE peliculas_db;

-- Tabla de películas
CREATE TABLE peliculas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    anio INT NOT NULL,
    duracion INT NOT NULL,
    pais VARCHAR(100) NOT NULL,
    director VARCHAR(150) NOT NULL,
    genero VARCHAR(100) NOT NULL,
    sinopsis TEXT,
    portada VARCHAR(500)
);

-- Tabla de actores
CREATE TABLE actores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    pais_nacimiento VARCHAR(100) NOT NULL
);

-- Relación muchos a muchos
CREATE TABLE pelicula_actor (
    pelicula_id BIGINT NOT NULL,
    actor_id BIGINT NOT NULL,

    PRIMARY KEY (pelicula_id, actor_id),

    FOREIGN KEY (pelicula_id)
        REFERENCES peliculas(id)
        ON DELETE CASCADE,

    FOREIGN KEY (actor_id)
        REFERENCES actores(id)
        ON DELETE CASCADE
);
