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
public class plan_operativo {
    private int id_plan;
    private String anio;
    private int id_usuario;
    private double presupuesto;
    private Timestamp fecha_creacion;
    private int estado;

    public plan_operativo() {
    }
    
    public plan_operativo(String anio, int id_usuario, double presupuesto) {
        this.anio = anio;
        this.id_usuario = id_usuario;
        this.presupuesto = presupuesto;
    }

    public int getId_plan() {
        return id_plan;
    }

    public void setId_plan(int id_plan) {
        this.id_plan = id_plan;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public double getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(double presupuesto) {
        this.presupuesto = presupuesto;
    }

    public Timestamp getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Timestamp fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "plan_operativo{" + "id_plan=" + id_plan + ", anio=" + anio + ", id_usuario=" + id_usuario + ", presupuesto=" + presupuesto + ", fecha_creacion=" + fecha_creacion + ", estado=" + estado + '}';
    }
    
}
