/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mnort.noticia.servicio;

import com.mnort.noticia.entidades.Imagen;
import com.mnort.noticia.entidades.Periodista;
import com.mnort.noticia.entidades.Usuario;
import com.mnort.noticia.enumeraciones.Rol;
import com.mnort.noticia.exceptions.MiException;
import com.mnort.noticia.repositorio.PeriodistaRepositorio;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PeriodistaServicio {
    @Autowired
    PeriodistaRepositorio periodistaRepo;
    
    @Autowired
    ImagenServicio imagenServicio;
    
     public List<Periodista> listarPeriodistas() {

        List<Periodista> periodistas = new ArrayList(periodistaRepo.todosPeriodistas());

        return periodistas;
    }

    @Transactional
    public void registrar(MultipartFile archivo, String nombre, String email, String password, String password2) throws MiException {

        validar(nombre, email, password, password2);

        Periodista periodista = new Periodista();

        periodista.setNombre(nombre);
        periodista.setEmail(email);
        periodista.setActivo(false);

        periodista.setPassword(new BCryptPasswordEncoder().encode(password));

        periodista.setRol(Rol.PERIODISTA);

        Imagen imagen = imagenServicio.guardar(archivo);

        periodista.setImagen(imagen);

        periodistaRepo.save(periodista);
    }
    
    private void validar(String nombre, String email, String password, String password2) throws MiException {

        if (nombre.isEmpty() || nombre == null) {

            throw new MiException("El nombre no puede ser nulo o estar vacio");
        }
        if (email.isEmpty() || email == null || !email.contains("@")) {

            throw new MiException("El email no puede ser nulo o estar vacio");
        }
        if (password.isEmpty() || password == null || password.length() < 5) {

            throw new MiException("La contraseña no puede ser nula o estar vacia y debe tener al menos 5 caracteres");
        }
        if (!password.equals(password2)) {
            throw new MiException("Las contraseñas ingresadas deben ser iguales");
        }
    }
    
}
