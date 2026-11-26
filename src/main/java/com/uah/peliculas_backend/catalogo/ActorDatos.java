package com.uah.peliculas_backend.catalogo;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

record ActorDatos(
        @NotBlank @Size(max = 150) String nombre,
        @NotNull @PastOrPresent LocalDate fechaNacimiento,
        @NotBlank @Size(max = 100) String paisNacimiento) {
}
