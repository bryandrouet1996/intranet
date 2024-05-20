/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinarp;

/**
 *
 * @author USUARIO
 */
public class Cedula {

    private String dato;
    private String fecha;

    public Cedula() {
    }

    public Cedula(String dato, String fecha) {
        this.dato = dato;
        this.fecha = fecha;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

}
