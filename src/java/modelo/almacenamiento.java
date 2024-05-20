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
public class almacenamiento {
    private int id_almacenamiento;
    private int id_usuario;
    private String nombre;
    private int tipo_contenido;
    private Timestamp fecha_creacion;
    private Timestamp fecha_update;

    public almacenamiento() {
    }

    public almacenamiento(String nombre, int tipo_contenido) {
        this.nombre = nombre;
        this.tipo_contenido = tipo_contenido;
    }
    
    public almacenamiento(int id_usuario, String nombre, int tipo_contenido) {
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.tipo_contenido = tipo_contenido;
    }
    
    public int getId_almacenamiento() {
        return id_almacenamiento;
    }

    public void setId_almacenamiento(int id_almacenamiento) {
        this.id_almacenamiento = id_almacenamiento;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTipo_contenido() {
        return tipo_contenido;
    }

    public void setTipo_contenido(int tipo_contenido) {
        this.tipo_contenido = tipo_contenido;
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
        return "almacenamiento{" + "id_almacenamiento=" + id_almacenamiento + ", id_usuario=" + id_usuario + ", nombre=" + nombre + ", tipo_contenido=" + tipo_contenido + ", fecha_creacion=" + fecha_creacion + ", fecha_update=" + fecha_update + '}';
    }
    
}
