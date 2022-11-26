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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Hernan
 */
@Controller
@PreAuthorize("hasAnyRole('ROLE_PERIODISTA', 'ROLE_ADMIN')")
@RequestMapping("/periodista")
public class PeriodistaControlador {

    @Autowired
    private NoticiaServicio noticiaServicio;

    @PreAuthorize("hasAnyRole('ROLE_PERIODISTA', 'ROLE_ADMIN')")
    @GetMapping("/dashboard")
    public String panelAdministrativo(ModelMap modelo) {

        return "panel.html";
    }

    @GetMapping("/crear") //metodo crear noticia el admin tambien es redirigido aquí gracias a su acceso
    public String crear() {

        return "noticiaFormulario";
    }

    @PostMapping("/creando") // CREANDO LA NOTICIA con los parametros recibidos del FORMULARIO
    public String creando(@RequestParam String titulo, @RequestParam String cuerpo, @RequestParam String imagen, ModelMap modelo, HttpSession session) {

        try {
            // obtengo informacion de la session y lo almaceno en creador
            Usuario creador = (Usuario) session.getAttribute("usuarioSession");

            noticiaServicio.crearNoticia(titulo, cuerpo, imagen, creador);

            modelo.put("exito", "La Noticia fue cargada con éxito.");

            return "panel.html";

        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());
            return "noticiaFormulario";
        }
    }

    @GetMapping("/listaCRUD")
    public String listar(ModelMap modelo) {

        List<Noticia> noticias = noticiaServicio.listarNoticias();

        modelo.put("noticias", noticias);

        return "noticiaCRUD.html";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo, HttpSession session) {

        // obtengo informacion de la session y lo almaceno en creador
        Usuario sesionActual = (Usuario) session.getAttribute("usuarioSession");
        Noticia noticia = noticiaServicio.getOne(id);

        Usuario creador = noticia.getCreador();

        System.out.println("Buscando el id del creador");
       
        try {
            if (creador.getId() != null) {
                System.out.println("TIENE ID");
                if (creador.getId().equals(sesionActual.getId())) {
                    modelo.put("noticia", noticiaServicio.getOne(id));

                    return "noticiaModificar.html";
                } else {
                    throw new MiException("Solo el creador de la noticia y el administrador pueden modificarla.");
                }
            }
        } catch (NullPointerException | MiException e) {
            if (sesionActual.getRol().toString().equals("ADMIN")) {
                modelo.put("noticia", noticiaServicio.getOne(id));
                return "noticiaModificar.html";
            } else {
                try {
                    throw new MiException("Solo el Administrador puede modificar esta noticia");
                } catch (MiException ex) {
                    modelo.put("error", ex.getMessage());
                    return "noticiaCRUD.html";
                }
            }
        }
        return "noticiaCRUD.html";
    }

    // Nota: un periodista solo podra modificar las noticias que le pertenencen validacion en el metodo anterior
    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, String titulo, String cuerpo, String imagen, ModelMap modelo) {
        try {
            noticiaServicio.modificarNoticia(id, titulo, cuerpo, imagen);
            modelo.put("exito", "Noticia modificada con exito");

            return "redirect:/periodista/listaCRUD";
            //return "noticia_list.html";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "noticiaModificar.html";
        }
    }
}
