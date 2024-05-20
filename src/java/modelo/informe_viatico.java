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
public class informe_viatico {
    private int id_informe;
    private int id_viatico;
    private String numero_solicitud;
    private String descripcion;
    private java.sql.Date fecha;
    private int id_estado;

    public informe_viatico() {
    }

    public informe_viatico(int id_viatico, String numero_solicitud, String descripcion, Date fecha, int id_estado) {
        this.id_viatico = id_viatico;
        this.numero_solicitud = numero_solicitud;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.id_estado = id_estado;
    }

    public int getId_informe() {
        return id_informe;
    }

    public void setId_informe(int id_informe) {
        this.id_informe = id_informe;
    }

    public int getId_viatico() {
        return id_viatico;
    }

    public void setId_viatico(int id_viatico) {
        this.id_viatico = id_viatico;
    }

    public String getNumero_solicitud() {
        return numero_solicitud;
    }

    public void setNumero_solicitud(String numero_solicitud) {
        this.numero_solicitud = numero_solicitud;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getId_estado() {
        return id_estado;
    }

    public void setId_estado(int id_estado) {
        this.id_estado = id_estado;
    }

    @Override
    public String toString() {
        return "informe_viatico{" + "id_informe=" + id_informe + ", id_viatico=" + id_viatico + ", numero_solicitud=" + numero_solicitud + ", descripcion=" + descripcion + ", fecha=" + fecha + ", id_estado=" + id_estado + '}';
    }
}
