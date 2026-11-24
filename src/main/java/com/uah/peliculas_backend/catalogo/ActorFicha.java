package com.uah.peliculas_backend.catalogo;

import java.time.LocalDate;

record ActorFicha(Long id, String nombre, LocalDate fechaNacimiento, String paisNacimiento) {
    static ActorFicha desde(Actor actor) {
        return new ActorFicha(actor.id, actor.nombre, actor.fechaNacimiento, actor.paisNacimiento);
    }
}
