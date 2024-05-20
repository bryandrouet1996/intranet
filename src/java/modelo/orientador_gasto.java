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
public class orientador_gasto {
    private int id_orientador;
    private String descripcion;
    private boolean estado;
    private Timestamp fecha_creacion;

    public orientador_gasto() {
    }

    public orientador_gasto(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId_orientador() {
        return id_orientador;
    }

    public void setId_orientador(int id_orientador) {
        this.id_orientador = id_orientador;
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
        return "orientador_gasto{" + "id_orientador=" + id_orientador + ", descripcion=" + descripcion + ", estado=" + estado + ", fecha_creacion=" + fecha_creacion + '}';
    }
}
