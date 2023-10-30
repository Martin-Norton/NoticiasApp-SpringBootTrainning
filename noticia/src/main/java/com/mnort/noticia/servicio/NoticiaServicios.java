/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mnort.noticia.servicio;

import com.mnort.noticia.entidades.Noticia;
import com.mnort.noticia.entidades.Periodista;
import com.mnort.noticia.exceptions.MiException;
import com.mnort.noticia.repositorio.NoticiaRepo;
import com.mnort.noticia.repositorio.PeriodistaRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NoticiaServicios {

    @Autowired
    NoticiaRepo noticiaRepo;
    
    @Autowired
    PeriodistaRepositorio periodistaRepo;

    @Transactional
    public void crearNoticia(String titulo, String cuerpo, String idPeriodista) throws MiException {
        UsuarioServicio us;
        
        Optional<Periodista> respuesta = periodistaRepo.findById(idPeriodista);
        
        Periodista periodista = null;
        
        if (respuesta.isPresent()) {
            periodista = respuesta.get();
        }
        
        validar(titulo, cuerpo);
        
        Noticia noticia = new Noticia();
        noticia.setTitulo(titulo);
        noticia.setCuerpo(cuerpo);
        noticia.setPeriodista(periodista);

        noticiaRepo.save(noticia);
    }
    
    public List<Noticia> listarNoticias(){
        List<Noticia> noticias = new ArrayList();

        noticias = noticiaRepo.findAll();

        return noticias;
    }
    
    @Transactional
    public void modificarNoticia(Long id, String titulo , String cuerpo) throws MiException {
        
        validar(titulo , cuerpo);
        
        Optional<Noticia> respuesta = noticiaRepo.findById(id);

        if (respuesta.isPresent()) {
            
            Noticia noticia = respuesta.get();

            noticia.setTitulo(titulo);
            noticia.setCuerpo(cuerpo);

            noticiaRepo.save(noticia);
        }
    }
     public Noticia getOne(Long id){
        return noticiaRepo.getOne(id);
    }
      private void validar(String titulo, String cuerpo) throws MiException {

        if (titulo.isEmpty() || titulo == null) {
            throw new MiException("El titulo no puede ser nulo o estar vacio");
        }
        if (cuerpo.isEmpty() || cuerpo == null) {
            throw new MiException("El cuerpo no puede ser nulo o estar vacio");
        }
    }
    
    public void validarTitulo(String titulo) throws MiException{
        if (titulo.isEmpty() || titulo == null) {
            throw new MiException("El titulo no puede ser nulo o estar vacio");
        }
    }
}
