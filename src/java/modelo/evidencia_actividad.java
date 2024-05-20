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
public class evidencia_actividad {
    private int id_evidencia;
    private int id_actividad;
    private String nombre;
    private String ruta;

    public evidencia_actividad() {
    }

    public evidencia_actividad(int id_actividad, String nombre, String ruta) {
        this.id_actividad = id_actividad;
        this.nombre = nombre;
        this.ruta = ruta;
    }

    public int getId_evidencia() {
        return id_evidencia;
    }

    public void setId_evidencia(int id_evidencia) {
        this.id_evidencia = id_evidencia;
    }

    public int getId_actividad() {
        return id_actividad;
    }

    public void setId_actividad(int id_actividad) {
        this.id_actividad = id_actividad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    @Override
    public String toString() {
        return "evidencia_actividad{" + "id_evidencia=" + id_evidencia + ", id_actividad=" + id_actividad + ", nombre=" + nombre + ", ruta=" + ruta + '}';
    }
    
    
}
