package com.uah.peliculas_backend.catalogo;

import jakarta.persistence.*;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "peliculas")
public class Pelicula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, length = 200)
    String titulo;

    @Column(nullable = false)
    int anio;

    @Column(nullable = false)
    int duracion;

    @Column(nullable = false, length = 100)
    String pais;

    @Column(nullable = false, length = 150)
    String director;

    @Column(nullable = false, length = 100)
    String genero;

    @Column(columnDefinition = "TEXT")
    String sinopsis;

    @Column(length = 500)
    String portada;

    @ManyToMany
    @JoinTable(name = "pelicula_actor",
            joinColumns = @JoinColumn(name = "pelicula_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id"))
    Set<Actor> actores = new LinkedHashSet<>();

    protected Pelicula() {
    }

    void actualizar(PeliculaDatos datos, Collection<Actor> reparto) {
        titulo = datos.titulo().strip();
        anio = datos.anio();
        duracion = datos.duracion();
        pais = datos.pais().strip();
        director = datos.director().strip();
        genero = datos.genero().strip();
        sinopsis = datos.sinopsis();
        portada = datos.portada();
        actores.clear();
        actores.addAll(reparto);
    }
}
