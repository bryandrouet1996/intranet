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
public class AprobacionGastosPersonales {
    private int id;
    private int idGasto;
    private usuario aprueba;    
    private Timestamp creacion;

    public AprobacionGastosPersonales() {
    }

    public AprobacionGastosPersonales(usuario aprueba, Timestamp creacion) {
        this.aprueba = aprueba;
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

    public usuario getAprueba() {
        return aprueba;
    }

    public void setAprueba(usuario aprueba) {
        this.aprueba = aprueba;
    }

    public Timestamp getCreacion() {
        return creacion;
    }

    public void setCreacion(Timestamp creacion) {
        this.creacion = creacion;
    }

    
}
