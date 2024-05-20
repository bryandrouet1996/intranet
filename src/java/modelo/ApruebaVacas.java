/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author Rayner
 */
public class ApruebaVacas {
    private int id;
    private String codigoCargo;
    private String cargo;    
    private String descripcion;
    private boolean estado;

    public ApruebaVacas(int id, String codigoCargo, String cargo, String descripcion, boolean estado) {
        this.id = id;
        this.codigoCargo = codigoCargo;
        this.cargo = cargo;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public String getCodigoCargo() {
        return codigoCargo;
    }

    public void setCodigoCargo(String codigoCargo) {
        this.codigoCargo = codigoCargo;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
