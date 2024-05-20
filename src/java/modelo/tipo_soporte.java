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
public class tipo_soporte {
    private int id_tipo;
    private String descripcion;
    private boolean estado;
    private Timestamp fecha_creacion;

    public tipo_soporte() {
    }

    public tipo_soporte(String descripcion, boolean estado, Timestamp fecha_creacion) {
        this.descripcion = descripcion;
        this.estado = estado;
        this.fecha_creacion = fecha_creacion;
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

    @Override
    public String toString() {
        return "tipo_soporte{" + "id_tipo=" + id_tipo + ", descripcion=" + descripcion + ", estado=" + estado + ", fecha_creacion=" + fecha_creacion + '}';
    }
    
    
}
