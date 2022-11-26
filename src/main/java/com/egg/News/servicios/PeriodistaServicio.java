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

        // BUSCO EL USUARIO CON ESE ID Y ME LO ALMACENO EN RESPUESTA
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        // PROCEDEMOS A CASTEAR LOS DATOS QUE ESTAN ALMACENADOS EN RESPUESTA A PERIODISTA
        // SE QUE EL USUARIO QUE ME LLEGA ES PERIODISTA POR LA PREVIA FILTRACION EN LA VISTA HTML(TABLA)
        Periodista periodista = (Periodista) respuesta.get();
        periodista.setSueldoMensual(sueldo);

        //usuarioRepositorio.save(periodista); si persisto como usuario no voy a tener acceso al sueldo 
        periodistaRepositorio.save(periodista);  
        
    }

    public Periodista getOne(String id) {
        return periodistaRepositorio.getById(id);
    }
}
