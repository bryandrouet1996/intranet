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
public class atendido_soporte {
    private int id_atendido;
    private int id_solicitud;
    private int id_tecnico;
    private Timestamp fecha_atendido;

    public atendido_soporte() {
    }

    public atendido_soporte(int id_solicitud, int id_tecnico) {
        this.id_solicitud = id_solicitud;
        this.id_tecnico = id_tecnico;
    }

    public int getId_atendido() {
        return id_atendido;
    }

    public void setId_atendido(int id_atendido) {
        this.id_atendido = id_atendido;
    }

    public int getId_solicitud() {
        return id_solicitud;
    }

    public void setId_solicitud(int id_solicitud) {
        this.id_solicitud = id_solicitud;
    }

    public int getId_tecnico() {
        return id_tecnico;
    }

    public void setId_tecnico(int id_tecnico) {
        this.id_tecnico = id_tecnico;
    }

    public Timestamp getFecha_atendido() {
        return fecha_atendido;
    }

    public void setFecha_atendido(Timestamp fecha_atendido) {
        this.fecha_atendido = fecha_atendido;
    }

    @Override
    public String toString() {
        return "atendido_soporte{" + "id_atendido=" + id_atendido + ", id_solicitud=" + id_solicitud + ", id_tecnico=" + id_tecnico + ", fecha_atendido=" + fecha_atendido + '}';
    }
    
    
}
