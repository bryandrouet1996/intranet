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
public class AnulacionVacaciones {

    private int id_anulacion;
    private int id_permiso;
    private int id_usuario;
    private int id_motivo;
    private String motivo;
    private Timestamp fecha_registro;

    public AnulacionVacaciones() {
    }

    public AnulacionVacaciones(int id_permiso, int id_usuario, int id_motivo) {
        this.id_permiso = id_permiso;
        this.id_usuario = id_usuario;
        this.id_motivo = id_motivo;
    }

    public int getId_anulacion() {
        return id_anulacion;
    }

    public void setId_anulacion(int id_anulacion) {
        this.id_anulacion = id_anulacion;
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

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public int getId_motivo() {
        return id_motivo;
    }

    public void setId_motivo(int id_motivo) {
        this.id_motivo = id_motivo;
    }

    
}
