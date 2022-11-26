package com.egg.News.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Hernan
 */

@Entity
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class Noticia implements Serializable {
    
    // Atributos 
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String titulo;
    @Column(columnDefinition="TEXT")
    private String cuerpo;
    private String imagen;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date alta;
    
    // agregamos el nuevo atributo y el tipo de relacion
    @ManyToOne
    private Periodista creador;

    
    // Constructores agregados con Lombok linea 24
  
    // Metodos Getter y Setter agregados con Lombok linea 25
}