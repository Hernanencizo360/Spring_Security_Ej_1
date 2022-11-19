package com.egg.News.entidades;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 *
 * @author Hernan
 */

@Entity
@PrimaryKeyJoinColumn(name="usuarioId")
public class Periodista extends Usuario{
    
    private Integer sueldoMensual;
    @OneToMany
    private List<Noticia> misNoticias = new ArrayList();
    
}
