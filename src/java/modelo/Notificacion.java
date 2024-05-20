/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Date;

/**
 *
 * @author USUARIO
 */
public class Notificacion {

    private int id;
    private String tipoDocumento;
    private String numeroDocumento;
    private int ciu;
    private String identificacion;
    private String razonSocial;
    private String identificacionRepresentante;
    private String nombreRepresentante;
    private float valor;
    private String fecha;

    public Notificacion() {
    }

    public Notificacion(int id, String tipoDocumento, String numeroDocumento, int ciu, String identificacion, String razonSocial, String identificacionRepresentante, String nombreRepresentante, float valor, String fecha) {
        this.id = id;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.ciu = ciu;
        this.identificacion = identificacion;
        this.razonSocial = razonSocial;
        this.identificacionRepresentante = identificacionRepresentante;
        this.nombreRepresentante = nombreRepresentante;
        this.valor = valor;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public int getCiu() {
        return ciu;
    }

    public void setCiu(int ciu) {
        this.ciu = ciu;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getIdentificacionRepresentante() {
        return identificacionRepresentante;
    }

    public void setIdentificacionRepresentante(String identificacionRepresentante) {
        this.identificacionRepresentante = identificacionRepresentante;
    }

    public String getNombreRepresentante() {
        return nombreRepresentante;
    }

    public void setNombreRepresentante(String nombreRepresentante) {
        this.nombreRepresentante = nombreRepresentante;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

}
