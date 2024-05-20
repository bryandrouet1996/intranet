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
public class asignacion_presupuesto {
    private int id_asignacion;
    private int id_organizacion;
    private double presupuesto;
    private boolean estado;
    private Timestamp fecha_asignacion;
    private Timestamp fecha_update;
    private int anio;

    public asignacion_presupuesto() {
    }
    
    public asignacion_presupuesto(int id_organizacion, double presupuesto, int anio) {
        this.id_organizacion = id_organizacion;
        this.presupuesto = presupuesto;
        this.anio = anio;
    }

    public int getId_asignacion() {
        return id_asignacion;
    }

    public void setId_asignacion(int id_asignacion) {
        this.id_asignacion = id_asignacion;
    }

    public int getId_organizacion() {
        return id_organizacion;
    }

    public void setId_organizacion(int id_organizacion) {
        this.id_organizacion = id_organizacion;
    }

    public double getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(double presupuesto) {
        this.presupuesto = presupuesto;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Timestamp getFecha_asignacion() {
        return fecha_asignacion;
    }

    public void setFecha_asignacion(Timestamp fecha_asignacion) {
        this.fecha_asignacion = fecha_asignacion;
    }

    public Timestamp getFecha_update() {
        return fecha_update;
    }

    public void setFecha_update(Timestamp fecha_update) {
        this.fecha_update = fecha_update;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    @Override
    public String toString() {
        return "asignacion_presupuesto{" + "id_asignacion=" + id_asignacion + ", id_organizacion=" + id_organizacion + ", presupuesto=" + presupuesto + ", estado=" + estado + ", fecha_asignacion=" + fecha_asignacion + ", fecha_update=" + fecha_update + ", anio=" + anio + '}';
    }
    
}
