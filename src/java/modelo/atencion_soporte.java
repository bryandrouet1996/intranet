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
public class atencion_soporte {
    private int id_atencion;
    private int id_solicitud;
    private int id_tecnico;
    private Timestamp fecha_atencion;

    public atencion_soporte() {
    }

    public atencion_soporte(int id_solicitud, int id_tecnico) {
        this.id_solicitud = id_solicitud;
        this.id_tecnico = id_tecnico;
    }

    public int getId_atencion() {
        return id_atencion;
    }

    public void setId_atencion(int id_atencion) {
        this.id_atencion = id_atencion;
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

    public Timestamp getFecha_atencion() {
        return fecha_atencion;
    }

    public void setFecha_atencion(Timestamp fecha_atencion) {
        this.fecha_atencion = fecha_atencion;
    }

    @Override
    public String toString() {
        return "atencion_soporte{" + "id_atencion=" + id_atencion + ", id_solicitud=" + id_solicitud + ", id_tecnico=" + id_tecnico + ", fecha_atencion=" + fecha_atencion + '}';
    }
    
}
