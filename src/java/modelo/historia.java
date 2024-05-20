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
public class historia {
    private int id_historia;
    private int id_usuario;
    private String descripcion;
    private String ruta;
    private String nombre;
    private String token;
    private java.sql.Date fecha_subida;
    private String hora_subida;
    private int id_tipo;
    
    public historia() {
    }

    public historia(int id_usuario, String descripcion, String ruta, String nombre, String token, Date fecha_subida, String hora_subida, int id_tipo) {
        this.id_usuario = id_usuario;
        this.descripcion = descripcion;
        this.ruta = ruta;
        this.nombre = nombre;
        this.token = token;
        this.fecha_subida = fecha_subida;
        this.hora_subida = hora_subida;
        this.id_tipo = id_tipo;
    }

    public int getId_historia() {
        return id_historia;
    }

    public void setId_historia(int id_historia) {
        this.id_historia = id_historia;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getFecha_subida() {
        return fecha_subida;
    }

    public void setFecha_subida(Date fecha_subida) {
        this.fecha_subida = fecha_subida;
    }

    public String getHora_subida() {
        return hora_subida;
    }

    public void setHora_subida(String hora_subida) {
        this.hora_subida = hora_subida;
    }

    public int getId_tipo() {
        return id_tipo;
    }

    public void setId_tipo(int id_tipo) {
        this.id_tipo = id_tipo;
    }

    @Override
    public String toString() {
        return "historia{" + "id_historia=" + id_historia + ", id_usuario=" + id_usuario + ", descripcion=" + descripcion + ", ruta=" + ruta + ", nombre=" + nombre + ", token=" + token + ", fecha_subida=" + fecha_subida + ", hora_subida=" + hora_subida + ", id_tipo=" + id_tipo + '}';
    }
    
}
