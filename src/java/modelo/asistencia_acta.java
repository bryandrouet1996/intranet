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
public class asistencia_acta {
    private int id_asistencia;
    private int id_acta;
    private int id_usuario;
    private Timestamp fecha_creacion;
    private Timestamp fecha_update;

    public asistencia_acta() {
    }

    public asistencia_acta(int id_acta, int id_usuario) {
        this.id_acta = id_acta;
        this.id_usuario = id_usuario;
    }

    public int getId_asistencia() {
        return id_asistencia;
    }

    public void setId_asistencia(int id_asistencia) {
        this.id_asistencia = id_asistencia;
    }

    public int getId_acta() {
        return id_acta;
    }

    public void setId_acta(int id_acta) {
        this.id_acta = id_acta;
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
        return "asistencia_acta{" + "id_asistencia=" + id_asistencia + ", id_acta=" + id_acta + ", id_usuario=" + id_usuario + ", fecha_creacion=" + fecha_creacion + ", fecha_update=" + fecha_update + '}';
    }
    
}
