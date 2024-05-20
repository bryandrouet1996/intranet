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
 * @author JC
 */
public class tramite_funcionario {
    
    private int id_tramite;
    private int id_solicitud;
    private int id_usuario;
    private Timestamp fecha_recibe;
    private Date tiempo;
    private Timestamp fecha_despacha;
    private int estado;
    private String observacion;
    private int estado_solicitud;
    private int id_actividad;
    private int cantidad;
    
    public tramite_funcionario() {
    }

    public tramite_funcionario(int id_solicitud, int id_usuario, Timestamp fecha_recibe, Date tiempo, Timestamp fecha_despacha, int estado, String observacion, int estado_solicitud) {
        this.id_solicitud = id_solicitud;
        this.id_usuario = id_usuario;
        this.fecha_recibe = fecha_recibe;
        this.tiempo = tiempo;
        this.fecha_despacha = fecha_despacha;
        this.estado = estado;
        this.observacion = observacion;
        this.estado_solicitud = estado_solicitud;
    }

    public tramite_funcionario(int id_solicitud, int id_usuario, int estado, String observacion, int id_actividad) {
        this.id_solicitud = id_solicitud;
        this.id_usuario = id_usuario;
        this.estado = estado;
        this.observacion = observacion;
        this.id_actividad = id_actividad;
    }

    
    public int getId_tramite() {
        return id_tramite;
    }

    public void setId_tramite(int id_tramite) {
        this.id_tramite = id_tramite;
    }

    public int getId_solicitud() {
        return id_solicitud;
    }

    public void setId_solicitud(int id_solicitud) {
        this.id_solicitud = id_solicitud;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public Timestamp getFecha_recibe() {
        return fecha_recibe;
    }

    public void setFecha_recibe(Timestamp fecha_recibe) {
        this.fecha_recibe = fecha_recibe;
    }

    public Date getTiempo() {
        return tiempo;
    }

    public void setTiempo(Date tiempo) {
        this.tiempo = tiempo;
    }

    public Timestamp getFecha_despacha() {
        return fecha_despacha;
    }

    public void setFecha_despacha(Timestamp fecha_despacha) {
        this.fecha_despacha = fecha_despacha;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public int getEstado_solicitud() {
        return estado_solicitud;
    }

    public void setEstado_solicitud(int estado_solicitud) {
        this.estado_solicitud = estado_solicitud;
    }

    public int getId_actividad() {
        return id_actividad;
    }

    public void setId_actividad(int id_actividad) {
        this.id_actividad = id_actividad;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
  
}
