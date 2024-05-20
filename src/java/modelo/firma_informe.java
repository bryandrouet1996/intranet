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
public class firma_informe {
    private int id_firma;
    private int id_informe;
    private int id_responsable;
    private int id_jefe;

    public firma_informe() {
    }

    public firma_informe(int id_informe, int id_responsable, int id_jefe) {
        this.id_informe = id_informe;
        this.id_responsable = id_responsable;
        this.id_jefe = id_jefe;
    }

    public int getId_firma() {
        return id_firma;
    }

    public void setId_firma(int id_firma) {
        this.id_firma = id_firma;
    }

    public int getId_informe() {
        return id_informe;
    }

    public void setId_informe(int id_informe) {
        this.id_informe = id_informe;
    }

    public int getId_responsable() {
        return id_responsable;
    }

    public void setId_responsable(int id_responsable) {
        this.id_responsable = id_responsable;
    }

    public int getId_jefe() {
        return id_jefe;
    }

    public void setId_jefe(int id_jefe) {
        this.id_jefe = id_jefe;
    }

    @Override
    public String toString() {
        return "firma_informe{" + "id_firma=" + id_firma + ", id_informe=" + id_informe + ", id_responsable=" + id_responsable + ", id_jefe=" + id_jefe + '}';
    }
    
}
