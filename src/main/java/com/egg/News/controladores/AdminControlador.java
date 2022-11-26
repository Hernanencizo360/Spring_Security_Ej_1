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
    @Autowired
    PeriodistaServicio periodistaServicio;

    @GetMapping("/listaUsuarios")
    public String listarUsuarios(ModelMap modelo) {

        List<Usuario> usuarios = usuarioServicio.listarUsuarios();

        modelo.put("usuarios", usuarios);

        return "usuariosCRUD.html";
    }

    @GetMapping("/modificarRol/{id}")
    public String rol(@PathVariable String id, ModelMap modelo) {

        try {
            usuarioServicio.modificarRol(id);
            modelo.put("exito", "Usuario modificado satisfactoriamente.");
        }catch(MiException ef){
            modelo.put("error", ef.getMessage());
        }
        // si redirecciono no se muestra el mensaje solucionar luego, en cambio al mostrar periodistasCRUD si.
        return "redirect:/admin/listaUsuarios";
    }

    // listar los periodistas e inyectar a mi vista periodista CRUD
    @GetMapping("/listaPeriodistas")
    public String listar(ModelMap modelo) {

        List<Usuario> usuarios = usuarioServicio.listarPeriodistas();

        // inyeccion de la lista de usuarios Recibidas del repositorio
        modelo.put("usuarios", usuarios);

        return "periodistasCRUD.html";
    }

    @GetMapping("/listaAltaPeriodistas")
    public String listarAlta(ModelMap modelo) {

        List<Usuario> usuarios = usuarioServicio.listarPeriodistas();

        // inyeccion de la lista de usuarios Recibidas del repositorio
        modelo.put("usuarios", usuarios);

        return "periodistasListAlta.html";
    }
    
    @GetMapping("/listaSueldoPeriodistas")
    public String listarSueldo(ModelMap modelo) {

        List<Usuario> usuarios = usuarioServicio.listarPeriodistas();

        // inyeccion de la lista de usuarios Recibidas del repositorio
        modelo.put("usuarios", usuarios);

        return "periodistasListSueldo.html";
    }

    // RECEPCION DEL ID DEL USUARIO A MODIFICAR SUELDO
    @GetMapping("/modificarSueldo/{id}")
    public String sueldo(@PathVariable String id, ModelMap modelo) {
        
        // inyeccion en el html del usuario para mostrar sus datos.
        modelo.put("usuario", usuarioServicio.getOne(id));

        return "periodistaModificarSueldo.html";
    }

    //RECEPCION DEL USUARIO Y EL SUELDO INTRODUCIDOS EN EL FORMULARIO
    @PostMapping("/modificarSueldo/{id}")
    public String sueldo(@PathVariable String id, Integer sueldo, ModelMap modelo) {
        try {

            // LLAMO AL METODO QUE ESTA EN PeriodistaServicio
            periodistaServicio.modificarSueldo(id, sueldo);
            
            // Si no hay errores inyecto mensaje de exito
            modelo.put("exito", "Sueldo modificado con exito");
            return "periodistasCRUD.html";

        } catch (MiException ex) {
           // si hay errro inyecto mensaje de error y redirecciono al html para pedir los datos
            modelo.put("error", ex.getMessage());
            return "periodistaModificarSueldo.html";
        }
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
        return "redirect:/admin/listaAltaPeriodistas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id, ModelMap modelo) throws MiException {
        noticiaServicio.eliminarNoticia(id);

        modelo.put("exito", "Noticia eliminada satisfactoriamente.");
        return "noticiaCRUD.html";
    }
}
