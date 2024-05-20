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
public class tipo_permiso {
    private int id_tipo;
    private String descripcion;
    private boolean estado;
    private Timestamp fecha_creacion;
    private Timestamp fecha_update;

    public tipo_permiso() {
    }

    public tipo_permiso(String descripcion, boolean estado) {
        this.descripcion = descripcion;
        this.estado = estado;
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

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
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
        return "tipo_permiso{" + "id_tipo=" + id_tipo + ", descripcion=" + descripcion + ", estado=" + estado + ", fecha_creacion=" + fecha_creacion + ", fecha_update=" + fecha_update + '}';
    }
    
}
