package com.egg.News.servicios;

import com.egg.News.entidades.Periodista;
import com.egg.News.entidades.Rol;
import com.egg.News.entidades.Usuario;
import com.egg.News.excepciones.MiException;
import com.egg.News.repositorios.PeriodistaRepositorio;
import com.egg.News.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 * @author Hernan
 */
@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
  
    // MËTODO PARA VALIDAR LOS PARAMETROS RECIBIDOS DEL FORMULARIO
    private void validar(String nombre, String email, String password, String password2) throws MiException {
        if (nombre == null || nombre.isEmpty()) {
            throw new MiException("El nombre no puede ser nulo ni estar vacio.");
        }

        if (email == null || nombre.isEmpty()) {
            throw new MiException("El Email no puede ser nulo ni estar vacio.");
        }
        if (password == null || nombre.isEmpty() || password.length() <= 5) {
            throw new MiException("La contraseña no puede ser nulo ni estar vacio y debe contener mas de 5 dígitos.");
        }
        if (!password.equals(password2)) {
            throw new MiException("Las contraseñas deben ser iguales");
        }
    }

    // METODO PARA REGISTRAR UN USUARIO
    @Transactional
    public void registrar(String nombre, String email, String password, String password2) throws MiException {
        // llamo al metodo validar pasando los parametros recibidos del form
        validar(nombre, email, password, password2);

        // si pasa el metodo validar instancio un Usuario
        Usuario usuario = new Usuario();

        // Seteo el nombre email contraseña y el tipo;
        usuario.setNombreUsuario(nombre);
        usuario.setEmail(email);
        // cuando seteamos la clave la codificamos con el metodo configureGlobal de la clase SeguridadWeb
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setActivo(Boolean.TRUE);

        // seteo el alta con la fecha y hora de creacion y agrego el rol;
        usuario.setAlta(new Date());
        // cualquier usuario que se registre va a setearse por defecto como USER
        usuario.setRol(Rol.USER);

        // por ultimo persistimos el Usuario 
        usuarioRepositorio.save(usuario);

        // para poder autenticar al usuario hacemos que la clase UsuaruoServicio
        // implemente la interfaz UserDetailsService.
        // sobre escribimos el metodo loadUserByUsername el cual recibe el username de 
        // tipo String para que nosotros podamos autentificarlo.
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // en primer lugar vamos a buscar un usuario de nuestro dominio 
        // a traves del metodo que escribimos en nuestro repositorio
        // y transformarlo en un usuario del dominio de Spring Security.
        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);

        if (usuario != null) { // si el usuario existe procedemos
            // instanciamos un objeto de la Clase User
            // el constructor de la clase nos solicitara varios parametros
            // un nombre de usuario, contraseña y una lista de permisos

            // para los permisos nos creamos una lista que almacene objetos de la clase GrantedAuthority
            List<GrantedAuthority> permisos = new ArrayList();

            //posteriormente creamos permisos para un usuario --> per; 
            // lo instanciamos como SimpleGrantedAuthority y dentro del constructor
            // especificamos a quien le vamos a dar esos permisos --> "ROLE_" + getRol().
            GrantedAuthority per = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());

            // luego agregamos esos permisos a la lista creada mas arriba
            permisos.add(per);

            // agregamos los permisos al constructor junto con email y la contraseña            
            User user = new User(usuario.getEmail(), usuario.getPassword(), permisos);

            // atrapamos al usuario que ya esta autenticado y guardarlo en la session.
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            //Guardamos la solicitud en un objeto de la interfaz HttpSession
            HttpSession session = attr.getRequest().getSession(true);

            // en los datos de la session seteamos los atributos
            // en la varible session vamos a setear el atributo usuarioSession como llave y lo que va a contener 
            // es el valor con todos los datos del objeto usuario autentificado.
            session.setAttribute("usuarioSession", usuario);

            // y retornamos ese Usuario.
            return user;
        } else {
            return null;
        }
    }

    public void modificar(String id, String nombre, String email, String password, String password2) throws MiException {
        validar(nombre, email, password, password2);

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setNombreUsuario(nombre);
            usuario.setEmail(email);
            usuario.setPassword(password);

            usuarioRepositorio.save(usuario);
        } else {
            throw new MiException("No se encontro ningún usuario con ese ID");
        }
    }

    // nota queda por reseatear el sueldo cuando se da de baja o se modifica el rol a USER.
    public void modificarEstado(String id) throws MiException {
        if (id == null || id.isEmpty()) {
            throw new MiException("El id no puede ser nulo o estar vacio");
        }

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();

            if (usuario.getActivo().equals(Boolean.TRUE)) {
                usuario.setActivo(Boolean.FALSE);
                // CONDICIONAL SI EL ROL DEL USUARIO ES PERIODISTA: SI ES PUEDO CASTEAR Y ACCEDER AL METODO MODIFI SUELDO
                if (usuario.getRol().toString().equals("PERIODISTA")) {
                    Periodista periodista = (Periodista) usuario;
                    periodista.setSueldoMensual(null);
                }

            } else {
                usuario.setActivo(Boolean.TRUE);
            }
            // POR ULTIMO GUARDO LOS CAMBIOS 
            usuarioRepositorio.save(usuario);

        } else {
            throw new MiException("No se encontro ningún usuario con ese ID");
        }
    }

    public void modificarRol(String id) throws MiException {

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {

            if (respuesta.get().getRol().toString().equals("USER")) {
                Usuario periodista = new Periodista();
                periodista = respuesta.get();

                periodista.setRol(Rol.PERIODISTA);

                // NOTA IMPORTANTE AQUI SE LLAMA AL REPO Y LANZO LA QUERY QUE MODIFICA EL TIPO DE OBJETO 
                // CON ESO SOLUCIONO EL ERROR CREO QUE ERA WrongClassException Y POR TANTO TENGO ACCESO A LA LISTA NOTICIA Y SUELDO
                usuarioRepositorio.cambiarRol("Periodista", id);

            } else {
                respuesta.get().setRol(Rol.USER);
                usuarioRepositorio.cambiarRol("Usuario", id);
                usuarioRepositorio.eliminarSueldo(null, id);
            }

        } else {
            throw new MiException("No se encontro ningún usuario con ese ID");
        }
    }

    public List<Usuario> listarPeriodistas() {

        // creacion de una lista que almacenara los usuarios con rol periodista
        List<Usuario> usuarios = new ArrayList();

        //la lista va a contener los usuarios que me devuelva el repositorio 
        //EN UsuarioRepositorio hay una Query que me devuelve los usuarios con el ROL de periodista
        usuarios = usuarioRepositorio.buscarPeriodistas();

        // RETORTNO LA LISTA AL CONTROLADOR admin/listarPeridista para inyectarlos en una tabla
        return usuarios;
    }

    public List<Usuario> listarUsuarios() {

        List<Usuario> usuarios = new ArrayList();

        usuarios = usuarioRepositorio.findAll(Sort.by(Sort.Direction.ASC, "rol"));

        return usuarios;
    }

    public Usuario getOne(String id) {
        return usuarioRepositorio.getById(id);
    }
}
