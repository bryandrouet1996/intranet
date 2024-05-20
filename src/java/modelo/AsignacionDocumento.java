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
public class AsignacionDocumento {

    private int idDocumento;
    private Documento documento = new Documento();
    private int actual;
    private String actualNombre;
    private String actualCargo;
    private int nuevo;
    private String nuevoNombre;
    private String nuevoCargo;
    private String comentario;
    private Timestamp creacion;

    public AsignacionDocumento() {
    }

    public int getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public int getActual() {
        return actual;
    }

    public void setActual(int actual) {
        this.actual = actual;
    }

    public String getActualNombre() {
        return actualNombre;
    }

    public void setActualNombre(String actualNombre) {
        this.actualNombre = actualNombre;
    }

    public String getActualCargo() {
        return actualCargo;
    }

    public void setActualCargo(String actualCargo) {
        this.actualCargo = actualCargo;
    }

    public int getNuevo() {
        return nuevo;
    }

    public void setNuevo(int nuevo) {
        this.nuevo = nuevo;
    }

    public String getNuevoNombre() {
        return nuevoNombre;
    }

    public void setNuevoNombre(String nuevoNombre) {
        this.nuevoNombre = nuevoNombre;
    }

    public String getNuevoCargo() {
        return nuevoCargo;
    }

    public void setNuevoCargo(String nuevoCargo) {
        this.nuevoCargo = nuevoCargo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Timestamp getCreacion() {
        return creacion;
    }

    public void setCreacion(Timestamp creacion) {
        this.creacion = creacion;
    }
}
