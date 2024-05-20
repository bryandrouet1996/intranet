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
public class motivo_permiso {
    private int id_motivo;
    private String descripcion;
    private int id_justificativo;

    public motivo_permiso() {
    }

    public motivo_permiso(String descripcion) {
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

    public int getId_justificativo() {
        return id_justificativo;
    }

    public void setId_justificativo(int id_justificativo) {
        this.id_justificativo = id_justificativo;
    }
    
    @Override
    public String toString() {
        return "motivo_permiso{" + "id_motivo=" + id_motivo + ", descripcion=" + descripcion + '}';
    }
    
}
