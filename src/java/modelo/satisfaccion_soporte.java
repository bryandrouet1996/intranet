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
public class satisfaccion_soporte {
    private int id_satisfaccion;
    private String descripcion;
    private Timestamp fecha_creacion;

    public satisfaccion_soporte() {
    }

    public satisfaccion_soporte(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId_satisfaccion() {
        return id_satisfaccion;
    }

    public void setId_satisfaccion(int id_satisfaccion) {
        this.id_satisfaccion = id_satisfaccion;
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

    @Override
    public String toString() {
        return "satisfaccion_soporte{" + "id_satisfaccion=" + id_satisfaccion + ", descripcion=" + descripcion + ", fecha_creacion=" + fecha_creacion + '}';
    }
    
}
