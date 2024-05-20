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
public class adjunto_permisohora {
    private int id_adjunto;
    private int id_permiso;
    private String nombre;
    private String ruta;

    public adjunto_permisohora() {
    }

    public adjunto_permisohora(int id_permiso, String nombre, String ruta) {
        this.id_permiso = id_permiso;
        this.nombre = nombre;
        this.ruta = ruta;
    }

    public int getId_adjunto() {
        return id_adjunto;
    }

    public void setId_adjunto(int id_adjunto) {
        this.id_adjunto = id_adjunto;
    }

    public int getId_permiso() {
        return id_permiso;
    }

    public void setId_permiso(int id_permiso) {
        this.id_permiso = id_permiso;
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
        return "adjunto{" + "id_adjunto=" + id_adjunto + ", id_permiso=" + id_permiso + ", nombre=" + nombre + ", ruta=" + ruta + '}';
    }
    
}
