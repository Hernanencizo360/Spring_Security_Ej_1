package com.egg.News.servicios;

import com.egg.News.entidades.Periodista;
import com.egg.News.entidades.Usuario;
import com.egg.News.excepciones.MiException;
import com.egg.News.repositorios.PeriodistaRepositorio;
import com.egg.News.repositorios.UsuarioRepositorio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Hernan
 */
@Service
public class PeriodistaServicio {

    @Autowired
    private PeriodistaRepositorio periodistaRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    public void modificarSueldo(String id, Integer sueldo) throws MiException {
        if (id == null || id.isEmpty()) {
            throw new MiException("El id no puede ser nulo o estar vacio.");
        }
        if (sueldo == null) {
            throw new MiException("El sueldo no puede ser nulo");
        }

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Periodista periodista = (Periodista) respuesta.get();
            periodista.setSueldoMensual(sueldo);

            periodistaRepositorio.save(periodista);
        }
    }

    public Periodista getOne(String id) {
        return periodistaRepositorio.getById(id);
    }
}
