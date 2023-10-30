/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mnort.noticia.controlador;

import com.mnort.noticia.entidades.Noticia;
import com.mnort.noticia.entidades.Periodista;
import com.mnort.noticia.exceptions.MiException;
import com.mnort.noticia.repositorio.NoticiaRepo;
import com.mnort.noticia.repositorio.PeriodistaRepositorio;
import com.mnort.noticia.servicio.NoticiaServicios;
import com.mnort.noticia.servicio.PeriodistaServicio;
import com.mnort.noticia.servicio.UsuarioServicio;
import java.util.ArrayList;
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

@Controller
@RequestMapping("/noticia")
public class NoticiaController {

    @Autowired
    private NoticiaServicios noticiaServicios;
    
    @Autowired
    private PeriodistaRepositorio periodistaRepo;
    
    @Autowired
    private NoticiaRepo noticiarepo;
    
    @Autowired
    private UsuarioServicio usuarioServicio;

    @PreAuthorize("hasAnyRole('ROLE_PERIODISTA', 'ROLE_ADMIN')")
    @GetMapping("/registrar") //localhost:8080/noticia/registrar
    public String registrar() {
        return "noticia_form.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_PERIODISTA', 'ROLE_ADMIN')")
    @PostMapping("/registro")
    public String registro(@RequestParam String titulo, String cuerpo, String email, ModelMap modelo, HttpSession session) {
        
        Periodista periodista;
        periodista = (Periodista) session.getAttribute("usuariosession");
        modelo.put("periodista", periodista);
        try {
            noticiaServicios.crearNoticia(titulo, cuerpo, periodista.getId());
            
            modelo.put("exito", "La noticia fue cargada correctamente");
            
        } catch (MiException ex) {
            
            modelo.put("error", ex.getMessage());
            return "noticia_form.html";
        }
        return "redirect:/";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_PERIODISTA', 'ROLE_ADMIN')")
    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable Long id, ModelMap modelo) {
        modelo.put("noticia", noticiaServicios.getOne(id));

        return "noticia_modificar.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_PERIODISTA', 'ROLE_ADMIN')")
    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable Long id, @RequestParam String titulo, String cuerpo, ModelMap modelo) {
        try {
            noticiaServicios.modificarNoticia(id, titulo , cuerpo);

            return "redirect:../";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "noticia_modificar.html";
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_PERIODISTA', 'ROLE_ADMIN')")
    @GetMapping("/panelAdmin")
    public String panelAdmin(){
        return "noticia_panelAdmin.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_PERIODISTA', 'ROLE_ADMIN')")
    @GetMapping("/misNoticias/{id}")
    public String misNoticias(@RequestParam String id, ModelMap modelo){
        
        List<Noticia> misNoticias = noticiarepo.buscarPorPeriodista(id);
        
        modelo.addAttribute("misNoticias", misNoticias);
        
        return "misNoticias.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_PERIODISTA', 'ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/vista/{id}")
    public String verNoticia(@PathVariable("id") Long id, ModelMap model) {
        Noticia noticia = noticiaServicios.getOne(id);
        if (noticia == null) {
            return "noticia-no-encontrada"; // Manejar caso de noticia no encontrada
        }
        model.addAttribute("noticia", noticia);
        return "noticia_vista.html"; // Vista para mostrar la información de la noticia
    }
    
}
