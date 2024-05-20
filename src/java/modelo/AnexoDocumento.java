/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author USUARIO
 */
public class AnexoDocumento {
    private int id;
    private int idDocumento;
    private String nombre;
    private String path;

    public AnexoDocumento() {
    }

    public AnexoDocumento(int id, int idDocumento, String nombre, String path) {
        this.id = id;
        this.idDocumento = idDocumento;
        this.nombre = nombre;
        this.path = path;
    }        

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    
}
