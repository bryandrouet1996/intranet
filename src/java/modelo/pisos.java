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
public class pisos {
    private int id_piso;
    private String nombre_piso;

    public pisos(int id_piso, String nombre_piso) {
        this.id_piso = id_piso;
        this.nombre_piso = nombre_piso;
    }

    public int getId_piso() {
        return id_piso;
    }

    public void setId_piso(int id_piso) {
        this.id_piso = id_piso;
    }

    public String getNombre_piso() {
        return nombre_piso;
    }

    public void setNombre_piso(String nombre_piso) {
        this.nombre_piso = nombre_piso;
    }
    
    public pisos() {
    }
}
