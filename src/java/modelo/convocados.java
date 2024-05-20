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
public class convocados {
    private int id_convocado;
    private int id_convocatoria;
    private int id_usuario;
    private Timestamp fecha_creacion;
    private Timestamp fecha_update;

    public convocados() {
    }

    public convocados(int id_convocatoria, int id_usuario) {
        this.id_convocatoria = id_convocatoria;
        this.id_usuario = id_usuario;
    }

    public int getId_convocado() {
        return id_convocado;
    }

    public void setId_convocado(int id_convocado) {
        this.id_convocado = id_convocado;
    }

    public int getId_convocatoria() {
        return id_convocatoria;
    }

    public void setId_convocatoria(int id_convocatoria) {
        this.id_convocatoria = id_convocatoria;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
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
        return "convocados{" + "id_convocado=" + id_convocado + ", id_convocatoria=" + id_convocatoria + ", id_usuario=" + id_usuario + ", fecha_creacion=" + fecha_creacion + ", fecha_update=" + fecha_update + '}';
    }
    
}
