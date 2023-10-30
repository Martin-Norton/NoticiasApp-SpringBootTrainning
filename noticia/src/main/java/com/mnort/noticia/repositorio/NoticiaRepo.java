/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mnort.noticia.repositorio;

import com.mnort.noticia.entidades.Noticia;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticiaRepo extends JpaRepository<Noticia , Long> {
    
    @Query("SELECT n FROM Noticia n WHERE n.periodista.id=:id")
    public List<Noticia> buscarPorPeriodista(@Param("id") String id);
}
