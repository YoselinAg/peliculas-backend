package com.uah.peliculas_backend.catalogo;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

interface PeliculaRepositorio extends JpaRepository<Pelicula, Long> {
    @EntityGraph(attributePaths = "actores")
    @Query("""
            select p from Pelicula p
            where (:titulo = '' or locate(lower(:titulo), lower(p.titulo)) > 0)
              and (:genero = '' or locate(lower(:genero), lower(p.genero)) > 0)
              and (:actor = '' or exists (
                  select a.id from Pelicula candidata join candidata.actores a
                  where candidata.id = p.id and locate(lower(:actor), lower(a.nombre)) > 0
              ))
            order by p.id
            """)
    List<Pelicula> buscar(@Param("titulo") String titulo, @Param("genero") String genero,
                         @Param("actor") String actor);
}
