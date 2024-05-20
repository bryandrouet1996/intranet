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
public class firma_viatico {
    private int id_firma;
    private int id_viatico;
    private int id_responsable;
    private int id_autoridad;

    public firma_viatico() {
    }

    public firma_viatico(int id_viatico, int id_responsable, int id_autoridad) {
        this.id_viatico = id_viatico;
        this.id_responsable = id_responsable;
        this.id_autoridad = id_autoridad;
    }

    public int getId_firma() {
        return id_firma;
    }

    public void setId_firma(int id_firma) {
        this.id_firma = id_firma;
    }

    public int getId_viatico() {
        return id_viatico;
    }

    public void setId_viatico(int id_viatico) {
        this.id_viatico = id_viatico;
    }

    public int getId_responsable() {
        return id_responsable;
    }

    public void setId_responsable(int id_responsable) {
        this.id_responsable = id_responsable;
    }

    public int getId_autoridad() {
        return id_autoridad;
    }

    public void setId_autoridad(int id_autoridad) {
        this.id_autoridad = id_autoridad;
    }

    @Override
    public String toString() {
        return "firma_viatico{" + "id_firma=" + id_firma + ", id_viatico=" + id_viatico + ", id_responsable=" + id_responsable + ", id_autoridad=" + id_autoridad + '}';
    }
  
}
