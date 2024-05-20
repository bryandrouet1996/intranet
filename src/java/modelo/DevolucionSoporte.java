/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Timestamp;

/**
 *
 * @author Don Beta
 */
public class DevolucionSoporte {
    private int idDevolucion;
    private int idSoporte;
    private String motivo;
    private Timestamp fecha;

    public DevolucionSoporte() {
    }   
    
    public DevolucionSoporte(int idSoporte, String motivo) {
        this.idSoporte = idSoporte;
        this.motivo = motivo;
    }

    public DevolucionSoporte(int idDevolucion, int idSoporte, String motivo, Timestamp fecha) {
        this.idDevolucion = idDevolucion;
        this.idSoporte = idSoporte;
        this.motivo = motivo;
        this.fecha = fecha;
    }

    public int getIdDevolucion() {
        return idDevolucion;
    }

    public void setIdDevolucion(int idDevolucion) {
        this.idDevolucion = idDevolucion;
    }

    public int getIdSoporte() {
        return idSoporte;
    }

    public void setIdSoporte(int idSoporte) {
        this.idSoporte = idSoporte;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }
    
    
}
