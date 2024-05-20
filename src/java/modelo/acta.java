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
public class acta {
    private int id_acta;
    private int id_convocatoria;
    private java.sql.Date fecha_acta;
    private java.sql.Date fecha_convocatoria;
    private String asunto;
    private String lugar;
    private int id_medio;
    private String hora_inicio;
    private String hora_fin;
    private String orden_dia;
    private String desarrollo;
    private boolean estado;
    private Timestamp fecha_creacion;
    private Timestamp fecha_update;

    public acta() {
    }

    public acta(int id_convocatoria, Date fecha_acta, Date fecha_convocatoria, String asunto, String lugar, int id_medio, String hora_inicio, String hora_fin, String orden_dia, String desarrollo, boolean estado) {
        this.id_convocatoria = id_convocatoria;
        this.fecha_acta = fecha_acta;
        this.fecha_convocatoria = fecha_convocatoria;
        this.asunto = asunto;
        this.lugar = lugar;
        this.id_medio = id_medio;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
        this.orden_dia = orden_dia;
        this.desarrollo = desarrollo;
        this.estado = estado;
    }

    public int getId_acta() {
        return id_acta;
    }

    public void setId_acta(int id_acta) {
        this.id_acta = id_acta;
    }

    public int getId_convocatoria() {
        return id_convocatoria;
    }

    public void setId_convocatoria(int id_convocatoria) {
        this.id_convocatoria = id_convocatoria;
    }
    
    public Date getFecha_acta() {
        return fecha_acta;
    }

    public void setFecha_acta(Date fecha_acta) {
        this.fecha_acta = fecha_acta;
    }

    public Date getFecha_convocatoria() {
        return fecha_convocatoria;
    }

    public void setFecha_convocatoria(Date fecha_convocatoria) {
        this.fecha_convocatoria = fecha_convocatoria;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public int getId_medio() {
        return id_medio;
    }

    public void setId_medio(int id_medio) {
        this.id_medio = id_medio;
    }

    public String getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(String hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public String getHora_fin() {
        return hora_fin;
    }

    public void setHora_fin(String hora_fin) {
        this.hora_fin = hora_fin;
    }

    public String getOrden_dia() {
        return orden_dia;
    }

    public void setOrden_dia(String orden_dia) {
        this.orden_dia = orden_dia;
    }

    public String getDesarrollo() {
        return desarrollo;
    }

    public void setDesarrollo(String desarrollo) {
        this.desarrollo = desarrollo;
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

    public Timestamp getFecha_update() {
        return fecha_update;
    }

    public void setFecha_update(Timestamp fecha_update) {
        this.fecha_update = fecha_update;
    }

    @Override
    public String toString() {
        return "acta{" + "id_acta=" + id_acta + ", id_convocatoria=" + id_convocatoria + ", fecha_acta=" + fecha_acta + ", fecha_convocatoria=" + fecha_convocatoria + ", asunto=" + asunto + ", lugar=" + lugar + ", id_medio=" + id_medio + ", hora_inicio=" + hora_inicio + ", hora_fin=" + hora_fin + ", orden_dia=" + orden_dia + ", desarrollo=" + desarrollo + ", estado=" + estado + ", fecha_creacion=" + fecha_creacion + ", fecha_update=" + fecha_update + '}';
    }

}
