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
public class participacion_viatico{
    private int id_participacion;
    private int id_viatico;
    private int id_usuario;

    public participacion_viatico() {
    }

    public participacion_viatico(int id_viatico, int id_usuario) {
        this.id_viatico = id_viatico;
        this.id_usuario = id_usuario;
    }

    public int getId_participacion() {
        return id_participacion;
    }

    public void setId_participacion(int id_participacion) {
        this.id_participacion = id_participacion;
    }

    public int getId_viatico() {
        return id_viatico;
    }

    public void setId_viatico(int id_viatico) {
        this.id_viatico = id_viatico;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    @Override
    public String toString() {
        return "participacion_viatico{" + "id_participacion=" + id_participacion + ", id_viatico=" + id_viatico + ", id_usuario=" + id_usuario + '}';
    }

}
