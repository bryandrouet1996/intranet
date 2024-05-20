/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control_sectorial;

/**
 *
 * @author USUARIO
 */
public class Sector {

    private int id;
    private int id_parroquia;
    private Parroquia parroquia = new Parroquia();
    private String nombre;

    public Sector() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_parroquia() {
        return id_parroquia;
    }

    public void setId_parroquia(int id_parroquia) {
        this.id_parroquia = id_parroquia;
    }

    public Parroquia getParroquia() {
        return parroquia;
    }

    public void setParroquia(Parroquia parroquia) {
        this.parroquia = parroquia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String stringify() {
        return "{id: " + id + ",id_parroquia: " + id_parroquia + ",nombre: '" + nombre + "'}";
    }
}
