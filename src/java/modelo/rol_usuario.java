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
public class rol_usuario {
    private int id_rol;
    private String descripcion;

    public rol_usuario() {
    }

    public rol_usuario(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId_rol() {
        return id_rol;
    }

    public void setId_rol(int id_rol) {
        this.id_rol = id_rol;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "rol_usuario{" + "id_rol=" + id_rol + ", descripcion=" + descripcion + '}';
    }
    
}
