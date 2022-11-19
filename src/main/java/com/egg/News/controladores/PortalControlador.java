package com.egg.News.controladores;

import com.egg.News.entidades.Noticia;
import com.egg.News.entidades.Usuario;
import com.egg.News.excepciones.MiException;
import com.egg.News.servicios.NoticiaServicio;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 *
 * @author Hernan
 */
@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private NoticiaServicio noticiaServicio;

    @GetMapping("/") // especificamos la ruta donde interactua el usuario
    public String index(ModelMap model) {
        try {
            return "index"; // indicamos el path de nuestra pagina. Vamos a templates a crearla.
        } catch (Exception e) {
            model.put("error", e.getMessage());
            return "error"; // mas tarde crearemos un html para mostrar si surge errores
        }
    }

    // la primera pagina con la que se encuentra el usuario es el index posterior tendra la opcion de 
    // registrarse o loguearse. --> UsuarioControlado
    //anotacion para acceder a este controlador hay que estar logueado y eso lo hacemos con PreAuthorize
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_PERIODISTA', 'ROLE_ADMIN')")
    @GetMapping("/inicio") // especificamos la ruta donde interactua el usuario
    public String inicio(ModelMap model, HttpSession session) {
        
        try {  
            
            Usuario logueado = (Usuario) session.getAttribute("usuarioSession");
            
            if(logueado.getRol().toString().equals("ADMIN") || logueado.getRol().toString().equals("PERIODISTA") ){
                return "redirect:/periodista/dashboard";
            }else{
                return "inicio"; // si es usuario va a inicio sino dashboard
            }
           
        } catch (Exception e) {
            model.put("error", e.getMessage());
            return "error"; // mas tarde crearemos un html para mostrar si surge errores
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_PERIODISTA', 'ROLE_ADMIN')")
    @GetMapping("/listaNoticias") // especificamos la ruta donde interactua el usuario
    public String listaNoticias(ModelMap model) {
        try {
            List<Noticia> noticias = noticiaServicio.listarNoticias(); // buscar todas las noticias
            model.put("noticias", noticias); // agregamos al model la propiedad "noticias" y la variable

            return "noticia_list"; // indicamos el path de nuestra pagina. Vamos a templates a crearla.
        } catch (Exception e) {
            model.put("error", e.getMessage());
            return "error"; // mas tarde crearemos un html para mostrar si surge errores
        }
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_PERIODISTA', 'ROLE_ADMIN')")
    @GetMapping("/detalle/{id}")
    public String detalleNoticia(ModelMap model, @PathVariable("id") String id) {
        try {
            Noticia noticia = noticiaServicio.buscarNoticia(id);
            model.put("noticia", noticia);
            return "detalle";

        } catch (MiException e) {
            model.put("error", e.getMessage());
            return "error";
        } catch (Exception e) {
            model.put("error", e.getMessage());
            return "error";
        }
    }
}
