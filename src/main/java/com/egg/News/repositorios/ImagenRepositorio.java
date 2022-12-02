package com.egg.News.repositorios;

import com.egg.News.entidades.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Hernan
 */

public interface ImagenRepositorio extends JpaRepository<Imagen, String> {
    
}
