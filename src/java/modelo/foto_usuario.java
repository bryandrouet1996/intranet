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
public class foto_usuario {
    private int id_foto;
    private int id_usuario;
    private String ruta;
    private String nombre;

    public foto_usuario() {
    }
    
    public foto_usuario(int id_usuario, String ruta) {
        this.id_usuario = id_usuario;
        this.ruta = ruta;
    }

    public foto_usuario(int id_usuario, String ruta, String nombre) {
        this.id_usuario = id_usuario;
        this.ruta = ruta;
        this.nombre = nombre;
    }

    public int getId_foto() {
        return id_foto;
    }

    public void setId_foto(int id_foto) {
        this.id_foto = id_foto;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "foto_usuario{" + "id_foto=" + id_foto + ", id_usuario=" + id_usuario + ", ruta=" + ruta + ", nombre=" + nombre + '}';
    }
    
}
