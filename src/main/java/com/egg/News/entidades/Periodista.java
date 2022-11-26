package com.egg.News.entidades;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Hernan
 */

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Periodista extends Usuario{
    
    private Integer sueldoMensual;
    @OneToMany
    private List<Noticia> misNoticias; 
}
