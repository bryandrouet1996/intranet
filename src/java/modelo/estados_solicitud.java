/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author JC
 */
public class estados_solicitud {
    int id_estados_solicitud;
    String estados;

    public estados_solicitud() {
    }

    public estados_solicitud(int id_estados_solicitud, String estados) {
        this.id_estados_solicitud = id_estados_solicitud;
        this.estados = estados;
    }

    public int getId_estados_solicitud() {
        return id_estados_solicitud;
    }

    public void setId_estados_solicitud(int id_estados_solicitud) {
        this.id_estados_solicitud = id_estados_solicitud;
    }

    public String getEstados() {
        return estados;
    }

    public void setEstados(String estados) {
        this.estados = estados;
    }
    
    
    
}
