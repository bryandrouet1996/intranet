/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Timestamp;

/**
 *
 * @author Kevin Druet
 */
public class anular_convocatoria {
    private int id_anular;
    private int id_convocatoria;
    private String razon;
    private Timestamp fecha_regsitro;

    public anular_convocatoria() {
    }
    
    public anular_convocatoria(int id_convocatoria, String razon) {
        this.id_convocatoria = id_convocatoria;
        this.razon = razon;
    }

    public int getId_anular() {
        return id_anular;
    }

    public void setId_anular(int id_anular) {
        this.id_anular = id_anular;
    }

    public int getId_convocatoria() {
        return id_convocatoria;
    }

    public void setId_convocatoria(int id_convocatoria) {
        this.id_convocatoria = id_convocatoria;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }

    public Timestamp getFecha_regsitro() {
        return fecha_regsitro;
    }

    public void setFecha_regsitro(Timestamp fecha_regsitro) {
        this.fecha_regsitro = fecha_regsitro;
    }

    @Override
    public String toString() {
        return "anular_convocatoria{" + "id_anular=" + id_anular + ", id_convocatoria=" + id_convocatoria + ", razon=" + razon + ", fecha_regsitro=" + fecha_regsitro + '}';
    }
    
}
