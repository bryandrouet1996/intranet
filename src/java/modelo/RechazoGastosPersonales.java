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
public class RechazoGastosPersonales {

    private int id;
    private int idGasto;
    private usuario rechaza;
    private String motivo;
    private Timestamp creacion;

    public RechazoGastosPersonales() {
    }

    public RechazoGastosPersonales(usuario rechaza, String motivo, Timestamp creacion) {
        this.rechaza = rechaza;
        this.motivo = motivo;
        this.creacion = creacion;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
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

    public usuario getRechaza() {
        return rechaza;
    }

    public void setRechaza(usuario rechaza) {
        this.rechaza = rechaza;
    }

    public Timestamp getCreacion() {
        return creacion;
    }

    public void setCreacion(Timestamp creacion) {
        this.creacion = creacion;
    }

}
