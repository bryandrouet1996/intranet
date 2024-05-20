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
public class Barrio {

    private int id;
    private int id_sector;
    private Sector sector = new Sector();
    private String nombre;

    public Barrio() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_sector() {
        return id_sector;
    }

    public void setId_sector(int id_sector) {
        this.id_sector = id_sector;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String stringify() {
        return "{id: " + id + ",id_sector: " + id_sector + ",nombre: '" + nombre + "'}";
    }
}
