/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author Kevin Druet
 */
public class destino {
    private int id_destino;
    private String nombre_pais;
    private String nombre_ciudad;

    public destino() {
    }

    public destino(String nombre_pais, String nombre_ciudad) {
        this.nombre_pais = nombre_pais;
        this.nombre_ciudad = nombre_ciudad;
    }

    public int getId_destino() {
        return id_destino;
    }

    public void setId_destino(int id_destino) {
        this.id_destino = id_destino;
    }

    public String getNombre_pais() {
        return nombre_pais;
    }

    public void setNombre_pais(String nombre_pais) {
        this.nombre_pais = nombre_pais;
    }

    public String getNombre_ciudad() {
        return nombre_ciudad;
    }

    public void setNombre_ciudad(String nombre_ciudad) {
        this.nombre_ciudad = nombre_ciudad;
    }

    @Override
    public String toString() {
        return "destino{" + "id_destino=" + id_destino + ", nombre_pais=" + nombre_pais + ", nombre_ciudad=" + nombre_ciudad + '}';
    }
   
}
