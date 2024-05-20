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
public class cargo {
    private int id_cargo;
    private String codigo_cargo;
    private String descripcion;

    public cargo() {
    }

    public cargo(String codigo_cargo, String descripcion) {
        this.codigo_cargo = codigo_cargo;
        this.descripcion = descripcion;
    }

    public int getId_cargo() {
        return id_cargo;
    }

    public void setId_cargo(int id_cargo) {
        this.id_cargo = id_cargo;
    }

    public String getCodigo_cargo() {
        return codigo_cargo;
    }

    public void setCodigo_cargo(String codigo_cargo) {
        this.codigo_cargo = codigo_cargo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "cargo{" + "id_cargo=" + id_cargo + ", codigo_cargo=" + codigo_cargo + ", descripcion=" + descripcion + '}';
    }
   
}
