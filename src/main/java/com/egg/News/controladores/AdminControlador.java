package com.egg.News.controladores;

import com.egg.News.entidades.Usuario;
import com.egg.News.excepciones.MiException;
import com.egg.News.servicios.NoticiaServicio;
import com.egg.News.servicios.PeriodistaServicio;
import com.egg.News.servicios.UsuarioServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Hernan
 */

@Controller
@RequestMapping("/admin")
public class AdminControlador {

    @Autowired
    private NoticiaServicio noticiaServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired PeriodistaServicio periodistaServicio; 

    @GetMapping("/listaPeriodistas")
    public String listar(ModelMap modelo) {

        List<Usuario> usuarios = usuarioServicio.listarPeriodistas();

        modelo.put("usuarios", usuarios);

        return "periodistasCRUD.html";
    }

    @GetMapping("/modificarEstado/{id}")
    public String estado(@PathVariable String id, ModelMap modelo) {
        
        try {
            usuarioServicio.modificarEstado(id);
            modelo.put("exito", "Usuario modificado satisfactoriamente.");
        } catch (MiException ex) {
           modelo.put("error", ex.getMessage());
        }
        // si redirecciono no se muestra el mensaje solucionar luego, en cambio al mostrar periodistasCRUD si.
        return "redirect:/admin/listaPeriodistas";
    }
    
    

    @GetMapping("/modificarSueldo/{id}")
    public String sueldo(@PathVariable String id, ModelMap modelo) {
        modelo.put("usuario", usuarioServicio.getOne(id));

        return "periodistaModificarSueldo.html";
    }
    
    @PostMapping("/modificarSueldo/{id}")
    public String sueldo(@PathVariable String id, Integer sueldo, ModelMap modelo) {
        try {
            
            periodistaServicio.modificarSueldo(id, sueldo);
            modelo.put("exito", "Sueldo modificado con exito");
            return "periodistasCRUD.html";
            
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "periodistaModificarSueldo.html";
        }
    }
   
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id, ModelMap modelo) throws MiException {
        noticiaServicio.eliminarNoticia(id);

        modelo.put("exito", "Noticia eliminada satisfactoriamente.");
        return "noticiaCRUD.html";
    }
}
