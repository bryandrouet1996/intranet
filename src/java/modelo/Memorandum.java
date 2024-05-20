/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author Bryan Druet
 */
public class Memorandum {
    private int id;
    private int id_usuario;
    private int id_asignado;
    private String actividad;
    private String participantes;
    private Date fecha_limite;
    private String documento;
    private int notificado;
    private String adjunto;
    private String adjunto_final;
    private String descripcion;
    private String observacion;
     private String observacion_final;
    private String resultado;
    private String estado;
    private int dias_restantes;
    private Timestamp fecha_creacion;
    private Timestamp fecha_update;

    public Memorandum() {
    }

    public Memorandum(int id_usuario, int id_asignado, String actividad, String participantes, Date fecha_limite, String documento, String descripcion, String observacion, String resultado) {
        this.id_usuario = id_usuario;
        this.id_asignado = id_asignado;
        this.actividad = actividad;
        this.participantes = participantes;
        this.fecha_limite = fecha_limite;
        this.documento = documento;
        this.descripcion = descripcion;
        this.observacion = observacion;
        this.resultado = resultado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getParticipantes() {
        return participantes;
    }

    public void setParticipantes(String participantes) {
        this.participantes = participantes;
    }

    public Date getFecha_limite() {
        return fecha_limite;
    }

    public void setFecha_limite(Date fecha_limite) {
        this.fecha_limite = fecha_limite;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
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

    public int getDias_restantes() {
        return dias_restantes;
    }

    public void setDias_restantes(int dias_restantes) {
        this.dias_restantes = dias_restantes;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_asignado() {
        return id_asignado;
    }

    public void setId_asignado(int id_asignado) {
        this.id_asignado = id_asignado;
    }

    public String getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(String adjunto) {
        this.adjunto = adjunto;
    }

    public String getAdjunto_final() {
        return adjunto_final;
    }

    public void setAdjunto_final(String adjunto_final) {
        this.adjunto_final = adjunto_final;
    }

    public String getObservacion_final() {
        return observacion_final;
    }

    public void setObservacion_final(String observacion_final) {
        this.observacion_final = observacion_final;
    }

    public int getNotificado() {
        return notificado;
    }

    public void setNotificado(int notificado) {
        this.notificado = notificado;
    }

    @Override
    public String toString() {
        return "Memorandum{" + "id=" + id + ", id_usuario=" + id_usuario + ", id_asignado=" + id_asignado + ", actividad=" + actividad + ", participantes=" + participantes + ", fecha_limite=" + fecha_limite + ", documento=" + documento + ", adjunto=" + adjunto + ", adjunto_final=" + adjunto_final + ", descripcion=" + descripcion + ", observacion=" + observacion + ", observacion_final=" + observacion_final + ", resultado=" + resultado + ", estado=" + estado + ", dias_restantes=" + dias_restantes + ", fecha_creacion=" + fecha_creacion + ", fecha_update=" + fecha_update + '}';
    }
    
}
