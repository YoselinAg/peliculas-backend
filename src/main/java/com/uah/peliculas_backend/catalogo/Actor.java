package com.uah.peliculas_backend.catalogo;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "actores")
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, length = 150)
    String nombre;

    @Column(name = "fecha_nacimiento", nullable = false)
    LocalDate fechaNacimiento;

    @Column(name = "pais_nacimiento", nullable = false, length = 100)
    String paisNacimiento;

    protected Actor() {
    }

    void actualizar(ActorDatos datos) {
        nombre = datos.nombre().strip();
        fechaNacimiento = datos.fechaNacimiento();
        paisNacimiento = datos.paisNacimiento().strip();
    }
}
