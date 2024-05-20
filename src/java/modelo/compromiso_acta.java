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
public class compromiso_acta {
    private int id_compromiso;
    private int id_acta;
    private String descripcion;
    private int id_responsable;
    private int estado;
    private java.sql.Date fecha_cumplimiento;
    private String grado;
    private String accion;
    private Timestamp fecha_creacion;
    private Timestamp fecha_update;

    public compromiso_acta() {
    }

    public compromiso_acta(int id_acta, String descripcion, int id_responsable, int estado, java.sql.Date fecha_cumplimiento) {
        this.id_acta = id_acta;
        this.descripcion = descripcion;
        this.id_responsable = id_responsable;
        this.estado = estado;
        this.fecha_cumplimiento = fecha_cumplimiento;
    }

    public int getId_compromiso() {
        return id_compromiso;
    }

    public void setId_compromiso(int id_compromiso) {
        this.id_compromiso = id_compromiso;
    }

    public int getId_acta() {
        return id_acta;
    }

    public void setId_acta(int id_acta) {
        this.id_acta = id_acta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId_responsable() {
        return id_responsable;
    }

    public void setId_responsable(int id_responsable) {
        this.id_responsable = id_responsable;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public java.sql.Date getFecha_cumplimiento() {
        return fecha_cumplimiento;
    }

    public void setFecha_cumplimiento(java.sql.Date fecha_cumplimiento) {
        this.fecha_cumplimiento = fecha_cumplimiento;
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

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    @Override
    public String toString() {
        return "compromiso_acta{" + "id_compromiso=" + id_compromiso + ", id_acta=" + id_acta + ", descripcion=" + descripcion + ", id_responsable=" + id_responsable + ", estado=" + estado + ", fecha_cumplimiento=" + fecha_cumplimiento + ", grado=" + grado + ", accion=" + accion + ", fecha_creacion=" + fecha_creacion + ", fecha_update=" + fecha_update + '}';
    }
    
}
