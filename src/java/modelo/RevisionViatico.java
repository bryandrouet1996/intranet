/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Timestamp;

/**
 *
 * @author Don Beta
 */
public class RevisionViatico {

    private int id;
    private int idViatico;
    private int idUsuario;
    private String motivo;
    private Timestamp fecha;

    public RevisionViatico() {
    }

    public RevisionViatico(int id, int idViatico, int idUsuario, String motivo, Timestamp fecha) {
        this.id = id;
        this.idViatico = idViatico;
        this.idUsuario = idUsuario;
        this.motivo = motivo;
        this.fecha = fecha;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdViatico() {
        return idViatico;
    }

    public void setIdViatico(int idViatico) {
        this.idViatico = idViatico;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    
    
}
