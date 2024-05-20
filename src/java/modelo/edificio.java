/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author Luis Torres F
 */
public class edificio {
    private int id_edificio;
    private String nombre_edificio;

    public edificio(int id_edificio, String nombre_edificio) {
        this.id_edificio = id_edificio;
        this.nombre_edificio = nombre_edificio;
    }

    public int getId_edificio() {
        return id_edificio;
    }

    public void setId_edificio(int id_edificio) {
        this.id_edificio = id_edificio;
    }

    public String getNombre_edificio() {
        return nombre_edificio;
    }

    public void setNombre_edificio(String nombre_edificio) {
        this.nombre_edificio = nombre_edificio;
    }
    
    public edificio() {
    }
}
