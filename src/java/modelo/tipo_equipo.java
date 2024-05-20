/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author Luis Torres F
 */
public class tipo_equipo {
    private int id_tipo_equipo;
    private String tipo;

    public tipo_equipo(int id_tipo_equipo, String tipo) {
        this.id_tipo_equipo = id_tipo_equipo;
        this.tipo = tipo;
    }

    tipo_equipo() {
        
    }

    public int getId_tipo_equipo() {
        return id_tipo_equipo;
    }

    public void setId_tipo_equipo(int id_tipo_equipo) {
        this.id_tipo_equipo = id_tipo_equipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
