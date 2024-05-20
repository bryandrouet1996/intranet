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
public class SolicitudApi {
    private int id_solicitud;
    private int estado;
    private String observacion;

    public SolicitudApi() {
    }

    public SolicitudApi(int id_solicitud, int estado, String observacion) {
        this.id_solicitud = id_solicitud;
        this.estado = estado;
        this.observacion = observacion;
    }
        
    public int getId_solicitud() {
        return id_solicitud;
    }

    public void setId_solicitud(int id_solicitud) {
        this.id_solicitud = id_solicitud;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
    
    
}
