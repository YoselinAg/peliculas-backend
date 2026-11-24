package com.uah.peliculas_backend.catalogo;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
@Transactional(readOnly = true)
class CatalogoServicio {
    private final PeliculaRepositorio peliculas;
    private final ActorRepositorio actores;

    CatalogoServicio(PeliculaRepositorio peliculas, ActorRepositorio actores) {
        this.peliculas = peliculas;
        this.actores = actores;
    }

    List<PeliculaFicha> buscarPeliculas(String titulo, String genero, String actor) {
        return peliculas.buscar(titulo.strip(), genero.strip(), actor.strip()).stream()
                .map(PeliculaFicha::desde)
                .toList();
    }

    PeliculaFicha consultarPelicula(long id) {
        return PeliculaFicha.desde(peliculaPorId(id));
    }

    @Transactional
    PeliculaFicha crearPelicula(PeliculaDatos datos) {
        Pelicula pelicula = new Pelicula();
        pelicula.actualizar(datos, obtenerReparto(datos));
        return PeliculaFicha.desde(peliculas.save(pelicula));
    }

    @Transactional
    PeliculaFicha editarPelicula(long id, PeliculaDatos datos) {
        Pelicula pelicula = peliculaPorId(id);
        pelicula.actualizar(datos, obtenerReparto(datos));
        return PeliculaFicha.desde(pelicula);
    }

    @Transactional
    void eliminarPelicula(long id) {
        peliculas.delete(peliculaPorId(id));
    }

    List<ActorFicha> buscarActores(String nombre, String pais) {
        return actores.buscar(nombre.strip(), pais.strip()).stream()
                .map(ActorFicha::desde)
                .toList();
    }

    ActorFicha consultarActor(long id) {
        return ActorFicha.desde(actorPorId(id));
    }

    @Transactional
    ActorFicha crearActor(ActorDatos datos) {
        Actor actor = new Actor();
        actor.actualizar(datos);
        return ActorFicha.desde(actores.save(actor));
    }

    @Transactional
    ActorFicha editarActor(long id, ActorDatos datos) {
        Actor actor = actorPorId(id);
        actor.actualizar(datos);
        return ActorFicha.desde(actor);
    }

    @Transactional
    void eliminarActor(long id) {
        actores.delete(actorPorId(id));
    }

    private Pelicula peliculaPorId(long id) {
        return peliculas.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe la película " + id));
    }

    private Actor actorPorId(long id) {
        return actores.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el actor " + id));
    }

    private List<Actor> obtenerReparto(PeliculaDatos datos) {
        if (datos.actorIds() == null || datos.actorIds().isEmpty()) {
            return List.of();
        }
        List<Actor> reparto = actores.findAllById(datos.actorIds());
        if (reparto.size() != datos.actorIds().size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Uno o más actores del reparto no existen");
        }
        return reparto;
    }
}
