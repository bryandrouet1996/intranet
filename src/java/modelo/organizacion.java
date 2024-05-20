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
public class organizacion {
    private int id_organizacion;
    private String nivel_padre;
    private String nivel_hijo;
    private String nombre;

    public organizacion() {
    }

    public organizacion(int id_organizacion, String nivel_padre, String nivel_hijo, String nombre) {
        this.id_organizacion = id_organizacion;
        this.nivel_padre = nivel_padre;
        this.nivel_hijo = nivel_hijo;
        this.nombre = nombre;
    }

    public int getId_organizacion() {
        return id_organizacion;
    }

    public void setId_organizacion(int id_organizacion) {
        this.id_organizacion = id_organizacion;
    }

    public String getNivel_padre() {
        return nivel_padre;
    }

    public void setNivel_padre(String nivel_padre) {
        this.nivel_padre = nivel_padre;
    }

    public String getNivel_hijo() {
        return nivel_hijo;
    }

    public void setNivel_hijo(String nivel_hijo) {
        this.nivel_hijo = nivel_hijo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "organizacion{" + "id_organizacion=" + id_organizacion + ", nivel_padre=" + nivel_padre + ", nivel_hijo=" + nivel_hijo + ", nombre=" + nombre + '}';
    }
    
    
}
