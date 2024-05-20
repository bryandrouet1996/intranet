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
public class revision_vacaciones {
    private int id_revision;
    private int id_permiso;
    private int id_usuario;
    private String descripcion;
    private Timestamp fecha_registro;

    public revision_vacaciones() {
    }

    public revision_vacaciones(int id_permiso, int id_usuario) {
        this.id_permiso = id_permiso;
        this.id_usuario = id_usuario;
    }

    public int getId_revision() {
        return id_revision;
    }

    public void setId_revision(int id_revision) {
        this.id_revision = id_revision;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Timestamp getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(Timestamp fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    @Override
    public String toString() {
        return "revision_vacaciones{" + "id_revision=" + id_revision + ", id_permiso=" + id_permiso + ", id_usuario=" + id_usuario + ", descripcion=" + descripcion + ", fecha_registro=" + fecha_registro + '}';
    }

}
