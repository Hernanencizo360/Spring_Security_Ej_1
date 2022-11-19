package com.egg.News.entidades;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 *
 * @author Hernan
 */

@Entity
@PrimaryKeyJoinColumn(name="usuarioId")
public class Administrador extends Usuario{
    
}
