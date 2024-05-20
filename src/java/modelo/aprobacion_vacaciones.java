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
public class aprobacion_vacaciones {
    private int id_aprobacion;
    private int id_permiso;
    private int id_usuario;
    private Timestamp fecha_registro;

    public aprobacion_vacaciones() {
    }

    public aprobacion_vacaciones(int id_permiso, int id_usuario) {
        this.id_permiso = id_permiso;
        this.id_usuario = id_usuario;
    }

    public int getId_aprobacion() {
        return id_aprobacion;
    }

    public void setId_aprobacion(int id_aprobacion) {
        this.id_aprobacion = id_aprobacion;
    }

    public int getId_permiso() {
        return id_permiso;
    }

    public void setId_permiso(int id_permiso) {
        this.id_permiso = id_permiso;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public Timestamp getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(Timestamp fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    @Override
    public String toString() {
        return "aprobacion_vacaciones{" + "id_aprobacion=" + id_aprobacion + ", id_permiso=" + id_permiso + ", id_usuario=" + id_usuario + ", fecha_registro=" + fecha_registro + '}';
    }

}
