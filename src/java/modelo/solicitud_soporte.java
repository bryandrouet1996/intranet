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
public class solicitud_soporte {
    private int id_solicitud;
    private int id_usuario;
    private int id_solicitante;
    private int id_sugerido;
    private int id_tipo;
    private int id_forma;
    private Timestamp fecha_creacion;
    private String incidente;
    private int estado;
    private String adjunto;
    private int referencia;
    private String descripcionRef;
    private String solucionRef;
    private String adjuntoRef;

    public solicitud_soporte() {
    }
    
    public solicitud_soporte(int id_solicitante, int id_sugerido, int id_tipo, int id_forma, String incidente) {
        this.id_solicitante = id_solicitante;
        this.id_sugerido = id_sugerido;
        this.id_tipo = id_tipo;
        this.id_forma = id_forma;
        this.incidente = incidente;
    }

    public solicitud_soporte(int id_usuario, int id_solicitante, int id_sugerido, int id_tipo, int id_forma, String incidente) {
        this.id_usuario = id_usuario;
        this.id_solicitante = id_solicitante;
        this.id_sugerido = id_sugerido;
        this.id_tipo = id_tipo;
        this.id_forma = id_forma;
        this.incidente = incidente;
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

    public int getId_solicitante() {
        return id_solicitante;
    }

    public void setId_solicitante(int id_solicitante) {
        this.id_solicitante = id_solicitante;
    }

    public int getId_sugerido() {
        return id_sugerido;
    }

    public void setId_sugerido(int id_sugerido) {
        this.id_sugerido = id_sugerido;
    }

    public int getId_tipo() {
        return id_tipo;
    }

    public void setId_tipo(int id_tipo) {
        this.id_tipo = id_tipo;
    }

    public int getId_forma() {
        return id_forma;
    }

    public void setId_forma(int id_forma) {
        this.id_forma = id_forma;
    }

    public Timestamp getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Timestamp fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public String getIncidente() {
        return incidente;
    }

    public void setIncidente(String incidente) {
        this.incidente = incidente;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
    
    

    @Override
    public String toString() {
        return "solicitud_soporte{" + "id_solicitud=" + id_solicitud + ", id_usuario=" + id_usuario + ", id_solicitante=" + id_solicitante + ", id_sugerido=" + id_sugerido + ", id_tipo=" + id_tipo + ", id_forma=" + id_forma + ", fecha_creacion=" + fecha_creacion + ", incidente=" + incidente + ", estado=" + estado + '}';
    }

    public String getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(String adjunto) {
        this.adjunto = adjunto;
    }

    public int getReferencia() {
        return referencia;
    }

    public void setReferencia(int referencia) {
        this.referencia = referencia;
    }

    public String getDescripcionRef() {
        return descripcionRef;
    }

    public void setDescripcionRef(String descripcionRef) {
        this.descripcionRef = descripcionRef;
    }

    public String getSolucionRef() {
        return solucionRef;
    }

    public void setSolucionRef(String solucionRef) {
        this.solucionRef = solucionRef;
    }

    public String getAdjuntoRef() {
        return adjuntoRef;
    }

    public void setAdjuntoRef(String adjuntoRef) {
        this.adjuntoRef = adjuntoRef;
    }
    
    
}
