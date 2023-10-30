/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mnort.noticia.controlador;

import com.mnort.noticia.entidades.Noticia;
import com.mnort.noticia.entidades.Usuario;
import com.mnort.noticia.exceptions.MiException;
import com.mnort.noticia.servicio.NoticiaServicios;
import com.mnort.noticia.servicio.PeriodistaServicio;
import com.mnort.noticia.servicio.UsuarioServicio;
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
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
public class PortalController {

    @Autowired
    NoticiaServicios noticiaServicio;

    @Autowired
    UsuarioServicio usuarioServicio;
    
    @Autowired
    PeriodistaServicio periodistaServicio;
    
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_PERIODISTA')")
    @GetMapping("/")
    public String index(ModelMap modelo, HttpSession session) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        
        if (logueado.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }

        List<Noticia> listanoticias = noticiaServicio.listarNoticias();

        modelo.addAttribute("noticias", listanoticias);

        return "index.html";
    }

    @GetMapping("/registrarUsuario")
    public String registrarUser() {
        return "registroUsuario.html";
    }
    
    @GetMapping("/registrarPeriodista")
    public String registrarPeriod() {
        return "registroPeriodista.html";
    }

    @PostMapping("/registroUsuario")
    public String registroUsuario(@RequestParam MultipartFile archivo, String nombre, @RequestParam String email, @RequestParam String password, String password2, ModelMap modelo) {
        try {
            
            usuarioServicio.registrar(archivo, nombre, email, password, password2);
            modelo.put("Exito", "usuario registrado correctamente");
            return "redirect:/index";
            
        } catch (MiException ex) {
            
            modelo.put("error", ex.getMessage());

            modelo.put("nombre", nombre);
            modelo.put("email", email);

            return "registroUsuario.html";
        }

    }
    @PostMapping("/registroPeriodista")
    public String registroPeriodista(@RequestParam MultipartFile archivo, String nombre, @RequestParam String email, @RequestParam String password, String password2, ModelMap modelo) {
        try {
            
            periodistaServicio.registrar(archivo, nombre, email, password, password2);
            modelo.put("Exito", "usuario registrado correctamente");
            return "redirect:/";
            
        } catch (MiException ex) {
            
            modelo.put("error", ex.getMessage());

            modelo.put("nombre", nombre);
            modelo.put("email", email);

            return "registroPeriodista.html";
        }

    }

    @GetMapping("/login")
    public String login(@RequestParam(required=false)String error, ModelMap modelo){
        if (error != null) {
            modelo.put("error", "usuario o contraseña invalidos");
        }
        return "login.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_PERIODISTA')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session) {
        
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        
        if (logueado.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }
        if (logueado.getRol().toString().equals("PERIODISTA")) {
            return "redirect:/noticia/panelAdmin";
        }
        
           return "inicio.html";
    }
    
    @GetMapping("/logout")
    public String logout(){
        return "login.html";
    
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_PERIODISTA')")
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo, HttpSession session){
        
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.put("usuario", usuario);
        
        return "usuario_modificar.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN','ROLE_PERIODISTA')")
    @PostMapping("/perfil/{id}")
    public String actualizar(@RequestParam MultipartFile archivo, @PathVariable String id, String nombre, @RequestParam String email, @RequestParam String password, String password2, ModelMap modelo){
        
        try {
            usuarioServicio.actualizar(id, archivo, nombre, email, password, password2);
            
            modelo.put("exito", "usuario actualizado correctamente");
            
            return "index.html";
        } catch (MiException ex) {
            
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
           
            return "usuario_modificar.html";
        }
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PERIODISTA')")
    @GetMapping("/misNoticias")
    public String misNoticias(){
        return "misNoticias.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PERIODISTA')")
    @GetMapping("/panelAdmin")
    public String admin(){
        return "noticia_panelAdmin.html";
    }


}
