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
public class tipo_viatico {
    private int id_viatico;
    private String descripcion;

    public tipo_viatico() {
    }

    public tipo_viatico(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId_viatico() {
        return id_viatico;
    }

    public void setId_viatico(int id_viatico) {
        this.id_viatico = id_viatico;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "tipo_viatico{" + "id_viatico=" + id_viatico + ", descripcion=" + descripcion + '}';
    }
    
}
