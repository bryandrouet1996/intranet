/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Timestamp;

/**
 *
 * @author Rayner
 */
public class TipoSolucion {
    private int idTipoSol;
    private String descripcion;
    private boolean estado;
    private Timestamp fechaCreacion;

    public TipoSolucion() {
    }

    public TipoSolucion(int idTipoSol, String descripcion, boolean estado, Timestamp fechaCreacion) {
        this.idTipoSol = idTipoSol;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public int getIdTipoSol() {
        return idTipoSol;
    }

    public void setIdTipoSol(int idTipoSol) {
        this.idTipoSol = idTipoSol;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    
    
}
