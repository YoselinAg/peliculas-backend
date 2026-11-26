package com.uah.peliculas_backend.catalogo;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/actores")
class ActorControlador {
    private final CatalogoServicio servicio;

    ActorControlador(CatalogoServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    List<ActorFicha> listar(@RequestParam(name = "nombre", defaultValue = "") String nombre,
                            @RequestParam(name = "paisNacimiento", defaultValue = "") String paisNacimiento) {
        return servicio.buscarActores(nombre, paisNacimiento);
    }

    @GetMapping("/{id}")
    ActorFicha consultar(@PathVariable("id") long id) {
        return servicio.consultarActor(id);
    }

    @PostMapping
    ResponseEntity<ActorFicha> crear(@Valid @RequestBody ActorDatos datos) {
        ActorFicha ficha = servicio.crearActor(datos);
        return ResponseEntity.created(URI.create("/api/actores/" + ficha.id())).body(ficha);
    }

    @PutMapping("/{id}")
    ActorFicha editar(@PathVariable("id") long id, @Valid @RequestBody ActorDatos datos) {
        return servicio.editarActor(id, datos);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void eliminar(@PathVariable("id") long id) {
        servicio.eliminarActor(id);
    }
}
