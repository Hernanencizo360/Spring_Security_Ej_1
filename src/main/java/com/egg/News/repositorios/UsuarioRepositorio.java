package com.egg.News.repositorios;

import com.egg.News.entidades.Usuario;
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
public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {
    
    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Usuario buscarPorEmail(@Param("email")String email);
    
    @Query("SELECT u FROM Usuario u WHERE u.rol = 'PERIODISTA'")
    public List<Usuario> buscarPeriodistas();
    
}
