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
public class estado_usuario {
    private int id_estado;
    private int id_usuario;
    private java.sql.Date fecha_acceso;
    private String hora_acceso;

    public estado_usuario() {
    }

    public estado_usuario(int id_usuario, Date fecha_acceso, String hora_acceso) {
        this.id_usuario = id_usuario;
        this.fecha_acceso = fecha_acceso;
        this.hora_acceso = hora_acceso;
    }

    public int getId_estado() {
        return id_estado;
    }

    public void setId_estado(int id_estado) {
        this.id_estado = id_estado;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }
    
    public Date getFecha_acceso() {
        return fecha_acceso;
    }

    public void setFecha_acceso(Date fecha_acceso) {
        this.fecha_acceso = fecha_acceso;
    }

    public String getHora_acceso() {
        return hora_acceso;
    }

    public void setHora_acceso(String hora_acceso) {
        this.hora_acceso = hora_acceso;
    }

    @Override
    public String toString() {
        return "estado_usuario{" + "id_estado=" + id_estado + ", id_usuario=" + id_usuario+ ", fecha_acceso=" + fecha_acceso + ", hora_acceso=" + hora_acceso + '}';
    }
  
}
