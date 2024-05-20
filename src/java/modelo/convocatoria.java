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
public class convocatoria {
    private int id_convocatoria;
    private int id_convocador;
    private java.sql.Date fecha_convocatoria;
    private String asunto;
    private String lugar;
    private String hora_inicio;
    private String hora_fin;
    private String orden_dia;
    private int id_medio;
    private Timestamp fecha_creacion;
    private Timestamp fecha_update;

    public convocatoria() {
    }
    
    public convocatoria(Date fecha_convocatoria, int id_convocador, String asunto, String lugar, String hora_inicio, String hora_fin, String orden_dia) {
        this.fecha_convocatoria = fecha_convocatoria;
        this.id_convocador = id_convocador;
        this.asunto = asunto;
        this.lugar = lugar;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
        this.orden_dia = orden_dia;
    }

    public int getId_convocatoria() {
        return id_convocatoria;
    }

    public void setId_convocatoria(int id_convocatoria) {
        this.id_convocatoria = id_convocatoria;
    }

    public Date getFecha_convocatoria() {
        return fecha_convocatoria;
    }

    public void setFecha_convocatoria(Date fecha_convocatoria) {
        this.fecha_convocatoria = fecha_convocatoria;
    }

    public int getId_convocador() {
        return id_convocador;
    }

    public void setId_convocador(int id_convocador) {
        this.id_convocador = id_convocador;
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

    public int getId_medio() {
        return id_medio;
    }

    public void setId_medio(int id_medio) {
        this.id_medio = id_medio;
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
        return "convocatoria{" + "id_convocatoria=" + id_convocatoria + ", id_convocador=" + id_convocador + ", fecha_convocatoria=" + fecha_convocatoria + ", asunto=" + asunto + ", lugar=" + lugar + ", hora_inicio=" + hora_inicio + ", hora_fin=" + hora_fin + ", orden_dia=" + orden_dia + ", id_medio=" + id_medio + ", fecha_creacion=" + fecha_creacion + ", fecha_update=" + fecha_update + '}';
    }
    
}
