package com.egg.News.servicios;

import com.egg.News.entidades.Noticia;
import com.egg.News.excepciones.MiException;
import com.egg.News.repositorios.NoticiaRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Hernan
 */
@Service
public class NoticiaServicio {

    @Autowired
    private NoticiaRepositorio noticiaRepositorio;

    private void validar(String titulo, String cuerpo, String urlDeImagen) throws MiException {
        if (titulo == null || titulo.isEmpty()) {
            throw new MiException("El ID del TITULO no puede ser nulo ni estar vacio.");
        }

        if (cuerpo == null || cuerpo.isEmpty()) {
            throw new MiException("El ID del CUERPO no puede ser nulo ni estar vacio.");
        }

        if (urlDeImagen == null || urlDeImagen.isEmpty()) {
            throw new MiException("la URL de la imagen no puede ser nulo ni estar vacio.");
        }
    }

    @Transactional
    public void crearNoticia(String titulo, String cuerpo, String urlImagen) throws MiException {

        validar(titulo, cuerpo, urlImagen);

        Noticia noticia = new Noticia();

        noticia.setTitulo(titulo);
        noticia.setCuerpo(cuerpo);
        noticia.setAlta(new Date());
        noticia.setImagen(urlImagen);

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

    @Transactional
    public void modificarNoticia(String id, String titulo, String cuerpo, String urlImagen) throws MiException {

        if (id == null || id.isEmpty()) {
            throw new MiException("El ID de la noticia no puede ser nulo ni estar vacio.");
        }

        validar(titulo, cuerpo, urlImagen);

        Optional<Noticia> respuesta = noticiaRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Noticia noticia = respuesta.get();
            noticia.setTitulo(titulo);
            noticia.setCuerpo(cuerpo);
            noticia.setImagen(urlImagen);

            noticiaRepositorio.save(noticia);
        } else {
            throw new MiException("No se encontro el ID del usuario solicitado");
        }
    }

    @Transactional
    public void eliminarNoticia(String id) throws MiException {
        Optional<Noticia> respuesta = noticiaRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Noticia noticia = respuesta.get();
            noticiaRepositorio.deleteById(noticia.getId());
        } else {
            throw new MiException("No existe una Noticia con ese ID");
        }
    }

    public List<Noticia> listarNoticias() {

        List<Noticia> noticias = new ArrayList();

        noticias = noticiaRepositorio.findAll(Sort.by(Sort.Direction.ASC, "alta"));
        
       
        return noticias;
    }
    
    public Noticia getOne(String id){
        return noticiaRepositorio.getById(id);
    }

}
