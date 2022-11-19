package com.egg.News.controladores;

import com.egg.News.excepciones.MiException;
import com.egg.News.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Hernan
 */
@Controller
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/registrar") // especificamos la ruta donde interactua el usuario
    public String registrar(ModelMap model) {
        try {
            return "usuario_form"; // indicamos el path de nuestra pagina. Vamos a templates a crearla.
        } catch (Exception e) {
            model.put("error", e.getMessage());
            return "error"; // mas tarde crearemos un html para mostrar si surge errores
        }
    }

    @PostMapping("/registro") // especificamos la ruta donde interactua el usuario
    public String registro(ModelMap model, @RequestParam String nombre, @RequestParam String email, @RequestParam String password, @RequestParam String password2) {
        try {

            usuarioServicio.registrar(nombre, email, password, password2);
            
            model.put("exito", "Ya puedes ingresar con tu correo y contraseña");

            return "index";
        } catch (MiException e) {
            model.put("error", e.getMessage());
            return "usuario_form"; // mas tarde crearemos un html para mostrar si surge errores
        }
    }

    @GetMapping("/login") // especificamos la ruta donde interactua el usuario
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {
        try {
            if (error != null) {
                modelo.put("error", "Usuario o contraseña invalido!");
            }
            return "usuario_login"; // indicamos el path de nuestra pagina. Vamos a templates a crearla.
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "index"; // mas tarde crearemos un html para mostrar si surge errores
        }
    }
}
