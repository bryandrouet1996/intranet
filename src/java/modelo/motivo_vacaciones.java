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
public class motivo_vacaciones {
    private int id_motivo;
    private String descripcion;

    public motivo_vacaciones() {
    }

    public motivo_vacaciones(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId_motivo() {
        return id_motivo;
    }

    public void setId_motivo(int id_motivo) {
        this.id_motivo = id_motivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "motivo_vaciones{" + "id_motivo=" + id_motivo + ", descripcion=" + descripcion + '}';
    }
    
}
