package com.egg.News.repositorios;

import com.egg.News.entidades.Usuario;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
    public Usuario buscarPorEmail(@Param("email") String email);

    @Query("SELECT u FROM Usuario u WHERE u.rol = 'PERIODISTA'")
    public List<Usuario> buscarPeriodistas();
    
    @Query("SELECT u FROM Usuario u WHERE u.rol = 'PERIODISTA' OR u.rol ='USER'")
    public List<Usuario> listarUsuariosYPeriodistas();

    //Query nativa sql para cambiar el dtype cuando se cambia el rol --> esto es importante ya que la modificacion de
    // Sueldo y de las Lista de noticias depende del tipo de objeto en este caso solo periodista tiene estos atributos
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value="UPDATE Usuario SET dtype = :tipo WHERE id = :id", nativeQuery=true)
    void cambiarRol(@Param("tipo") String tipo, @Param("id") String id);
    
    // Query para eliminar el sueldo o reseatear a null cuando se cambia el rol de periodista a usuario 
    // podria hacerse casteando el periodista a usuario? tema de herencia buscar info.
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value="UPDATE Usuario SET sueldo_mensual = :sueldo WHERE id = :id", nativeQuery=true)
    void eliminarSueldo(@Param("sueldo") Integer sueldo, @Param("id") String id);
    
}
