package com.egg.News.servicios;

import com.egg.News.entidades.Imagen;
import com.egg.News.entidades.Noticia;
import com.egg.News.entidades.Periodista;
import com.egg.News.entidades.Usuario;
import com.egg.News.excepciones.MiException;
import com.egg.News.repositorios.NoticiaRepositorio;
import com.egg.News.repositorios.PeriodistaRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Hernan
 */
@Service
public class NoticiaServicio {

    @Autowired
    private NoticiaRepositorio noticiaRepositorio;
    @Autowired
    private PeriodistaRepositorio periodistaRepositorio;
    @Autowired
    private ImagenServicio imagenServicio;

    private void validar(String titulo, String cuerpo) throws MiException {
        if (titulo == null || titulo.isEmpty()) {
            throw new MiException("El ID del TITULO no puede ser nulo ni estar vacio.");
        }

        if (cuerpo == null || cuerpo.isEmpty()) {
            throw new MiException("El ID del CUERPO no puede ser nulo ni estar vacio.");
        }

//        if (archivo == null || archivo.isEmpty()) {
//            throw new MiException("la URL de la imagen no puede ser nulo ni estar vacio.");
//        }
    }

    @Transactional
    public void crearNoticia(String titulo, String cuerpo, MultipartFile archivo, Usuario creador) throws MiException {

        validar(titulo, cuerpo);

        Noticia noticia = new Noticia();
        Imagen imagen = imagenServicio.guardar(archivo);

        noticia.setTitulo(titulo);
        noticia.setCuerpo(cuerpo);
        noticia.setAlta(new Date());
        noticia.setImagen(imagen);

        // retoque final de agregar id del creador periodista si fuera ADMIN no se inserta id Por la relacion de las entidades
        if (creador.getRol().toString().equals("PERIODISTA")) {

            System.out.println(">>>>>>>>>>>>ESTOY COMO ADMIN Y ESTOY DENTRO?<<<<<<<<<<<<<<<<<");
            System.out.println("");
            System.out.println("");

            Periodista periodista = new Periodista();

            periodista = (Periodista) creador;

            try {

                noticia.setCreador(periodista);

                // busco la lista de noticias que tiene ese Periodista agrego la nueva noticia y seteo los cambios.
                List<Noticia> noticias = noticiaRepositorio.buscarPorPeriodista(periodista.getId());
                noticias.add(noticia);

                periodista.setMisNoticias(noticias);

                // persisto la noticia y al periodista
                noticiaRepositorio.save(noticia);
                periodistaRepositorio.save(periodista);

            } catch (NullPointerException e) {
                System.out.println(e.getMessage());
            }
        }
        // Si es un admin el que crea la noticia la guardo sin idCreador la relacion es con periodista
        noticiaRepositorio.save(noticia);
    }

    @Transactional(readOnly = true)
    public Noticia buscarNoticia(String id) throws MiException {
        Optional<Noticia> respuesta = noticiaRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Noticia noticia = respuesta.get();
            return noticia;
        } else {
            throw new MiException("No existe una Noticia con ese ID");
        }
    }

    //Nota: agregue validaciones para que se pueda modificar solo las noticias que le pertenecen a cada periodista
    // dicha validacion en el controlador podria hacer un metodo en el servicio que se encargue de dicha tarea.
    @Transactional
    public void modificarNoticia(String id, String titulo, String cuerpo, MultipartFile archivo) throws MiException {

        if (id == null || id.isEmpty()) {
            throw new MiException("El ID de la noticia no puede ser nulo ni estar vacio.");
        }

        validar(titulo, cuerpo);

        Optional<Noticia> respuesta = noticiaRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Noticia noticia = respuesta.get();
            noticia.setTitulo(titulo);
            noticia.setCuerpo(cuerpo);
            
            String idImagen = null;
            
            if(noticia.getImagen() != null){
                idImagen = noticia.getImagen().getId();
            }
            
            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
            
            noticia.setImagen(imagen);

            noticiaRepositorio.save(noticia);
        } else {
            throw new MiException("No se encontro el ID de la noticia solicitado");
        }
    }

    @Transactional
    public void eliminarNoticia(String id) throws MiException {
        Optional<Noticia> respuesta = noticiaRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Noticia noticia = respuesta.get();

            Periodista periodista = new Periodista();

            periodista = noticia.getCreador();
            try {
                // para poder eliminar una noticia primeramente debo eliminar la relacion que existe con el periodista
                // es decir eliminar la FK de la tabla lista noticias.
                List<Noticia> noticias = noticiaRepositorio.buscarPorPeriodista(periodista.getId());

                Iterator<Noticia> it = noticias.iterator();

                while (it.hasNext()) {
                    Noticia aux = it.next();
                    if (aux.getId().equals(id)) {
                        it.remove();
                        break;
                    }
                }
                // en el bucle elimino la noticia de la lista seteo la lista actualizada al id del periodista.
                periodista.setMisNoticias(noticias);

                periodistaRepositorio.save(periodista);
            } catch (NullPointerException e) {
                System.out.println("Noticia creada por el admin");
            } finally {
                // <<ELIMINACION DE LA NOTICIA DE LA BASE DE DATOS>>
                noticiaRepositorio.deleteById(noticia.getId());
            }
        } else {
            throw new MiException("No existe una Noticia con ese ID");
        }
    }

    public List<Noticia> listarNoticias() {

        List<Noticia> noticias = new ArrayList();

        noticias = noticiaRepositorio.findAll(Sort.by(Sort.Direction.ASC, "alta"));

        return noticias;
    }

    public Noticia getOne(String id) {
        return noticiaRepositorio.getById(id);
    }
}
