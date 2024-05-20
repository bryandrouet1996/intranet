/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Timestamp;

/**
 *
 * @author Kevin Druet
 */
public class tipo_contenido {
    private int id_tipo;
    private String descripcion;
    private Timestamp fecha_creacion;
    private Timestamp fecha_update;

    public tipo_contenido() {
    }
    
    public tipo_contenido(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId_tipo() {
        return id_tipo;
    }

    public void setId_tipo(int id_tipo) {
        this.id_tipo = id_tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Timestamp getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Timestamp fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public Timestamp getFecha_update() {
        return fecha_update;
    }

    public void setFecha_update(Timestamp fecha_update) {
        this.fecha_update = fecha_update;
    }

    @Override
    public String toString() {
        return "tipo_contenido{" + "id_tipo=" + id_tipo + ", descripcion=" + descripcion + ", fecha_creacion=" + fecha_creacion + ", fecha_update=" + fecha_update + '}';
    }
    
}
