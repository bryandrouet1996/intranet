/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Timestamp;

/**
 *
 * @author USUARIO
 */
public class ValidacionGastosPersonales {
    private int id;
    private int idGasto;
    private usuario valida;
    private Timestamp creacion;

    public ValidacionGastosPersonales() {
    }

    public ValidacionGastosPersonales(usuario valida, Timestamp creacion) {
        this.valida = valida;
        this.creacion = creacion;
    }   
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdGasto() {
        return idGasto;
    }

    public void setIdGasto(int idGasto) {
        this.idGasto = idGasto;
    }

    public usuario getValida() {
        return valida;
    }

    public void setValida(usuario valida) {
        this.valida = valida;
    }

    public Timestamp getCreacion() {
        return creacion;
    }

    public void setCreacion(Timestamp creacion) {
        this.creacion = creacion;
    }
    
    
}
