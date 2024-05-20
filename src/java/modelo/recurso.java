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
public class recurso {
    private int id_recurso;
    private int id_almacenamiento;
    private int id_usuario;
    private String nombre;
    private String descripcion;
    private String ruta;
    private Timestamp fecha_creacion;
    private Timestamp fecha_update;

    public recurso() {
    }

    public recurso(String nombre, String descripcion, String ruta) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ruta = ruta;
    }

    public recurso(int id_almacenamiento, int id_usuario, String nombre, String descripcion) {
        this.id_almacenamiento = id_almacenamiento;
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    
    public recurso(int id_almacenamiento, int id_usuario, String nombre, String descripcion, String ruta) {
        this.id_almacenamiento = id_almacenamiento;
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ruta = ruta;
    }

    public int getId_recurso() {
        return id_recurso;
    }

    public void setId_recurso(int id_recurso) {
        this.id_recurso = id_recurso;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
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
        return "recurso{" + "id_recurso=" + id_recurso + ", id_almacenamiento=" + id_almacenamiento + ", id_usuario=" + id_usuario + ", nombre=" + nombre + ", descripcion=" + descripcion + ", ruta=" + ruta + ", fecha_creacion=" + fecha_creacion + ", fecha_update=" + fecha_update + '}';
    }
    
}
