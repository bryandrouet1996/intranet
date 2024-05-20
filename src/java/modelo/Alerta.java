/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.sql.Timestamp;

/**
 *
 * @author Bryan Druet
 */
public class Alerta {
    private int id;
    private int id_usuario;
    private int dias_notificacion;
    private Timestamp fecha_creacion;
    private Timestamp fecha_update;

    public Alerta() {
    }

    public Alerta(int id_usuario, int dias_notificacion) {
        this.id_usuario = id_usuario;
        this.dias_notificacion = dias_notificacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getDias_notificacion() {
        return dias_notificacion;
    }

    public void setDias_notificacion(int dias_notificacion) {
        this.dias_notificacion = dias_notificacion;
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
        return "Alerta{" + "id=" + id + ", id_usuario=" + id_usuario + ", dias_notificacion=" + dias_notificacion + ", fecha_creacion=" + fecha_creacion + ", fecha_update=" + fecha_update + '}';
    }
    
}
