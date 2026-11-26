package com.uah.peliculas_backend.catalogo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

interface ActorRepositorio extends JpaRepository<Actor, Long> {
    @Query("""
            select a from Actor a
            where (:nombre = '' or locate(lower(:nombre), lower(a.nombre)) > 0)
              and (:pais = '' or locate(lower(:pais), lower(a.paisNacimiento)) > 0)
            order by a.id
            """)
    List<Actor> buscar(@Param("nombre") String nombre, @Param("pais") String pais);
}
