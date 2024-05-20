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
public class comentario_actividad {
    private int id_comentario;
    private int id_usuario;
    private int id_actividad;
    private String descripcion;
    private java.sql.Date fecha_registro;

    public comentario_actividad() {
    }

    public comentario_actividad(int id_usuario, int id_actividad, String descripcion, java.sql.Date fecha_registro) {
        this.id_usuario = id_usuario;
        this.id_actividad = id_actividad;
        this.descripcion = descripcion;
        this.fecha_registro= fecha_registro;
    }
    
    public int getId_comentario() {
        return id_comentario;
    }

    public void setId_comentario(int id_comentario) {
        this.id_comentario = id_comentario;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_actividad() {
        return id_actividad;
    }

    public void setId_actividad(int id_actividad) {
        this.id_actividad = id_actividad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(Date fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    @Override
    public String toString() {
        return "comentario_actividad{" + "id_comentario=" + id_comentario + ", id_usuario=" + id_usuario + ", id_actividad=" + id_actividad + ", descripcion=" + descripcion + ", fecha_registro=" + fecha_registro + '}';
    }

}
