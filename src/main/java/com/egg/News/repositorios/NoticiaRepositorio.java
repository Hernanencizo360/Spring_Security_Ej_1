package com.egg.News.repositorios;

import com.egg.News.entidades.Noticia;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Hernan
 */
@Repository
public interface NoticiaRepositorio extends JpaRepository<Noticia, String>{
    
    @Query("SELECT n FROM Noticia n WHERE n.titulo = :titulo")
    public Noticia buscarPorTitulo(@Param("titulo") String titulo);
    
    // query para traer todas las noticias pertecientes a un periodista en particular.
    @Query("SELECT n FROM Noticia n WHERE n.creador.id = :id")
    public List<Noticia> buscarPorPeriodista(@Param ("id") String id); 
}
