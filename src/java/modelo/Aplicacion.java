/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author Don Beta
 */
public class Aplicacion {
    private int id;
    private String nombre;
    private String codigo;
    private String version;

    public Aplicacion() {
    }

    public Aplicacion(int id, String nombre, String codigo, String version) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
        this.version = version;
    }    

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    
}
