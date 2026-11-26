package com.uah.peliculas_backend.catalogo;

import java.util.Comparator;
import java.util.List;

record PeliculaFicha(Long id, String titulo, int anio, int duracion, String pais, String director,
                     String genero, String sinopsis, String portada, List<ActorFicha> actores) {
    static PeliculaFicha desde(Pelicula pelicula) {
        List<ActorFicha> reparto = pelicula.actores.stream()
                .map(ActorFicha::desde)
                .sorted(Comparator.comparing(ActorFicha::id))
                .toList();
        return new PeliculaFicha(pelicula.id, pelicula.titulo, pelicula.anio, pelicula.duracion,
                pelicula.pais, pelicula.director, pelicula.genero, pelicula.sinopsis,
                pelicula.portada, reparto);
    }
}
