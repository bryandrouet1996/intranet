/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobile;

/**
 *
 * @author USUARIO
 */
public class SubcategoriaSolicitud {

    private int id;
    private int idCategoria;
    private String nombre;

    public SubcategoriaSolicitud() {
    }

    public SubcategoriaSolicitud(int id, int idCategoria, String nombre) {
        this.id = id;
        this.idCategoria = idCategoria;
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "{\"id\":" + getId() + ",\"idCategoria\":" + getIdCategoria() + ",\"nombre\":\"" + getNombre() + "\"}";
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

}
