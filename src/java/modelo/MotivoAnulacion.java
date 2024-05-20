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
public class MotivoAnulacion {

    private int id;
    private String motivo;

    public MotivoAnulacion() {
    }

    public MotivoAnulacion(int id, String motivo) {
        this.id = id;
        this.motivo = motivo;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
