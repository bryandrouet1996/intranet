/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author Kevin Druet
 */
public class solicitud_tinta {
    private int id_solicitud;
    private int id_para;
    private String asunto;
    private java.sql.Date fecha_solicitud;
    private String detalle;
    private int id_solicitante;
    private Timestamp fecha_creacion;
    private Timestamp fecha_update;
    private int estado;

    public solicitud_tinta() {
    }

    public solicitud_tinta(int id_para, String asunto, Date fecha_solicitud, String detalle, int id_solicitante) {
        this.id_para = id_para;
        this.asunto = asunto;
        this.fecha_solicitud = fecha_solicitud;
        this.detalle = detalle;
        this.id_solicitante = id_solicitante;
    }

    public solicitud_tinta(int id_para, String asunto, Date fecha_solicitud, String detalle, int id_solicitante, int estado) {
        this.id_para = id_para;
        this.asunto = asunto;
        this.fecha_solicitud = fecha_solicitud;
        this.detalle = detalle;
        this.id_solicitante = id_solicitante;
        this.estado = estado;
    }

    public int getId_solicitud() {
        return id_solicitud;
    }

    public void setId_solicitud(int id_solicitud) {
        this.id_solicitud = id_solicitud;
    }

    public int getId_para() {
        return id_para;
    }

    public void setId_para(int id_para) {
        this.id_para = id_para;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public Date getFecha_solicitud() {
        return fecha_solicitud;
    }

    public void setFecha_solicitud(Date fecha_solicitud) {
        this.fecha_solicitud = fecha_solicitud;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public int getId_solicitante() {
        return id_solicitante;
    }

    public void setId_solicitante(int id_solicitante) {
        this.id_solicitante = id_solicitante;
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

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "solicitud_tinta{" + "id_solicitud=" + id_solicitud + ", id_para=" + id_para + ", asunto=" + asunto + ", fecha_solicitud=" + fecha_solicitud + ", detalle=" + detalle + ", id_solicitante=" + id_solicitante + ", fecha_creacion=" + fecha_creacion + ", fecha_update=" + fecha_update + ", estado=" + estado + '}';
    }
    
}
