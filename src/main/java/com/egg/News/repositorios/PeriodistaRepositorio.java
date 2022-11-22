package com.egg.News.repositorios;

import com.egg.News.entidades.Periodista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Hernan
 */

@Repository
public interface PeriodistaRepositorio extends JpaRepository<Periodista, String> {
    
}
