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
public class forma_soporte {
    private int id_forma;
    private String descripcion;
    private boolean estado;
    private Timestamp fecha_creacion;

    public forma_soporte() {
    }

    public forma_soporte(int id_forma, String descripcion, boolean estado, Timestamp fecha_creacion) {
        this.id_forma = id_forma;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fecha_creacion = fecha_creacion;
    }

    public int getId_forma() {
        return id_forma;
    }

    public void setId_forma(int id_forma) {
        this.id_forma = id_forma;
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
        return "forma_soporte{" + "id_forma=" + id_forma + ", descripcion=" + descripcion + ", estado=" + estado + ", fecha_creacion=" + fecha_creacion + '}';
    }
    
}
