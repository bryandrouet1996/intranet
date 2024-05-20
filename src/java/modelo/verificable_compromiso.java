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
public class verificable_compromiso {
    private int id_verificable;
    private int id_compromiso;
    private String nombre;
    private String ruta;
    private Timestamp fecha_registro;
    private Timestamp fecha_update;

    public verificable_compromiso() {
    }
    
    public verificable_compromiso(int id_compromiso, String nombre, String ruta) {
        this.id_compromiso = id_compromiso;
        this.nombre = nombre;
        this.ruta = ruta;
    }

    public int getId_verificable() {
        return id_verificable;
    }

    public void setId_verificable(int id_verificable) {
        this.id_verificable = id_verificable;
    }

    public int getId_compromiso() {
        return id_compromiso;
    }

    public void setId_compromiso(int id_compromiso) {
        this.id_compromiso = id_compromiso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public Timestamp getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(Timestamp fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public Timestamp getFecha_update() {
        return fecha_update;
    }

    public void setFecha_update(Timestamp fecha_update) {
        this.fecha_update = fecha_update;
    }

    @Override
    public String toString() {
        return "verificable_compromiso{" + "id_verificable=" + id_verificable + ", id_compromiso=" + id_compromiso + ", nombre=" + nombre + ", ruta=" + ruta + ", fecha_registro=" + fecha_registro + ", fecha_update=" + fecha_update + '}';
    }
    
}
