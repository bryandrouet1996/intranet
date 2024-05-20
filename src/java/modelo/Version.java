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
public class Version {
    private int id;
    private int idUsu;
    private int idApp;
    private String etiqueta;
    private int tipo;
    private String descripcion;
    private String adjunto;
    private Timestamp fecha;

    public Version() {
    }

    public Version(int id, int idUsu, int idApp, String etiqueta, int tipo, String descripcion, String adjunto, Timestamp fecha) {
        this.id = id;
        this.idUsu = idUsu;
        this.idApp = idApp;
        this.etiqueta = etiqueta;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.adjunto = adjunto;
        this.fecha = fecha;
    }    

    public Version(int idUsu, int idApp, int tipo, String descripcion) {
        this.idUsu = idUsu;
        this.idApp = idApp;
        this.tipo = tipo;
        this.descripcion = descripcion;
    }    

    public String getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(String adjunto) {
        this.adjunto = adjunto;
    }   
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsu() {
        return idUsu;
    }

    public void setIdUsu(int idUsu) {
        this.idUsu = idUsu;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public int getIdApp() {
        return idApp;
    }

    public void setIdApp(int idApp) {
        this.idApp = idApp;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
        
    
}
