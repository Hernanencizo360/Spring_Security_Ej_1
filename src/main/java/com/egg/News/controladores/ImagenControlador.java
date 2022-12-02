package com.egg.News.controladores;

import com.egg.News.entidades.Noticia;
import com.egg.News.excepciones.MiException;
import com.egg.News.servicios.NoticiaServicio;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Hernan
 */
@Controller
@RequestMapping("/imagen")
public class ImagenControlador {

    @Autowired
    private NoticiaServicio noticiaServicio;

    @GetMapping("/noticia/{id}")
    public ResponseEntity<byte[]> imagenNoticia(@PathVariable String id) {
        
        try {
            Noticia noticia = noticiaServicio.buscarNoticia(id);
            
            if(noticia.getImagen() == null){
                throw new MiException("La noticia no tiene imagen asignada");
            }
            
            byte[] imagen = noticia.getImagen().getContenido();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
            
        } catch (MiException ex) {
            Logger.getLogger(ImagenControlador.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
