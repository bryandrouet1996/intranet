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
public class documento_informe {
    private int id_documento;
    private int id_informe;
    private String razon_social;
    private String ruc;
    private String numero;
    private java.sql.Date fecha;
    private String descripcion;
    private double total;

    public documento_informe() {
    }

    public documento_informe(int id_informe, String razon_social, String ruc, String numero, Date fecha, String descripcion, double total) {
        this.id_informe = id_informe;
        this.razon_social = razon_social;
        this.ruc = ruc;
        this.numero = numero;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.total = total;
    }

    public int getId_documento() {
        return id_documento;
    }

    public void setId_documento(int id_documento) {
        this.id_documento = id_documento;
    }

    public int getId_informe() {
        return id_informe;
    }

    public void setId_informe(int id_informe) {
        this.id_informe = id_informe;
    }

    public String getRazon_social() {
        return razon_social;
    }

    public void setRazon_social(String razon_social) {
        this.razon_social = razon_social;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "documento_informe{" + "id_documento=" + id_documento + ", id_informe=" + id_informe + ", razon_social=" + razon_social + ", ruc=" + ruc + ", numero=" + numero + ", fecha=" + fecha + ", descripcion=" + descripcion + ", total=" + total + '}';
    }
    
}
