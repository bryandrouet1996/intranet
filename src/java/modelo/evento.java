/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Date;

/**
 *
 * @author Kevin Druet
 */
public class evento {
    private int id_evento;
    private int id_usuario;
    private String titulo;
    private String color;
    private java.sql.Date inicio;
    private String hora_inicio;
    private java.sql.Date fin;
    private String hora_fin;
    private java.sql.Date fecha_creacion;
    private int id_estado;

    public evento() {
    }

    public evento(int id_usuario, String titulo, String color, Date inicio, String hora_inicio, Date fin, String hora_fin, Date fecha_creacion, int id_estado) {
        this.id_usuario = id_usuario;
        this.titulo = titulo;
        this.color = color;
        this.inicio = inicio;
        this.hora_inicio = hora_inicio;
        this.fin = fin;
        this.hora_fin = hora_fin;
        this.fecha_creacion = fecha_creacion;
        this.id_estado = id_estado;
    }

    public int getId_evento() {
        return id_evento;
    }

    public void setId_evento(int id_evento) {
        this.id_evento = id_evento;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public java.sql.Date getInicio() {
        return inicio;
    }

    public void setInicio(java.sql.Date inicio) {
        this.inicio = inicio;
    }

    public java.sql.Date getFin() {
        return fin;
    }

    public void setFin(java.sql.Date fin) {
        this.fin = fin;
    }

    public java.sql.Date getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(java.sql.Date fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public int getId_estado() {
        return id_estado;
    }

    public void setId_estado(int id_estado) {
        this.id_estado = id_estado;
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

    @Override
    public String toString() {
        return "evento{" + "id_evento=" + id_evento + ", id_usuario=" + id_usuario + ", titulo=" + titulo + ", color=" + color + ", inicio=" + inicio + ", hora_inicio=" + hora_inicio + ", fin=" + fin + ", hora_fin=" + hora_fin + ", fecha_creacion=" + fecha_creacion + ", id_estado=" + id_estado + '}';
    }
   
}
