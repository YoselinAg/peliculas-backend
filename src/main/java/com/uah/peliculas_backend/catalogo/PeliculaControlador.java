package com.uah.peliculas_backend.catalogo;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/peliculas")
class PeliculaControlador {
    private final CatalogoServicio servicio;

    PeliculaControlador(CatalogoServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    List<PeliculaFicha> listar(@RequestParam(name = "titulo", defaultValue = "") String titulo,
                               @RequestParam(name = "genero", defaultValue = "") String genero,
                               @RequestParam(name = "actor", defaultValue = "") String actor) {
        return servicio.buscarPeliculas(titulo, genero, actor);
    }

    @GetMapping("/{id}")
    PeliculaFicha consultar(@PathVariable("id") long id) {
        return servicio.consultarPelicula(id);
    }

    @PostMapping
    ResponseEntity<PeliculaFicha> crear(@Valid @RequestBody PeliculaDatos datos) {
        PeliculaFicha ficha = servicio.crearPelicula(datos);
        return ResponseEntity.created(URI.create("/api/peliculas/" + ficha.id())).body(ficha);
    }

    @PutMapping("/{id}")
    PeliculaFicha editar(@PathVariable("id") long id, @Valid @RequestBody PeliculaDatos datos) {
        return servicio.editarPelicula(id, datos);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void eliminar(@PathVariable("id") long id) {
        servicio.eliminarPelicula(id);
    }
}
