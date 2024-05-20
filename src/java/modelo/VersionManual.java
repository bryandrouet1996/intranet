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
public class VersionManual {
    private int id;
    private int idUsuario;
    private String usuario;
    private int idManual;
    private String manual;
    private String tipo;
    private String version;
    private String titulo;
    private String descripcion;
    private String adjunto;
    private Timestamp creacion;

    public VersionManual() {
    }

    public VersionManual(int id, int idUsuario, String usuario, int idManual, String manual, String tipo, String version, String titulo, String descripcion, String adjunto, Timestamp creacion) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.idManual = idManual;
        this.manual = manual;
        this.tipo = tipo;
        this.version = version;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.adjunto = adjunto;
        this.creacion = creacion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getIdManual() {
        return idManual;
    }

    public void setIdManual(int idManual) {
        this.idManual = idManual;
    }

    public String getManual() {
        return manual;
    }

    public void setManual(String manual) {
        this.manual = manual;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(String adjunto) {
        this.adjunto = adjunto;
    }

    public Timestamp getCreacion() {
        return creacion;
    }

    public void setCreacion(Timestamp creacion) {
        this.creacion = creacion;
    }

    
    
}
