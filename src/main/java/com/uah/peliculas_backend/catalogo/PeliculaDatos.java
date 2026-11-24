package com.uah.peliculas_backend.catalogo;

import jakarta.validation.constraints.*;
import java.util.Set;

record PeliculaDatos(
        @NotBlank @Size(max = 200) String titulo,
        @NotNull @Positive Integer anio,
        @NotNull @Positive Integer duracion,
        @NotBlank @Size(max = 100) String pais,
        @NotBlank @Size(max = 150) String director,
        @NotBlank @Size(max = 100) String genero,
        String sinopsis,
        @Size(max = 500) String portada,
        Set<@NotNull @Positive Long> actorIds) {
}
