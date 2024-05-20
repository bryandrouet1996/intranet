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
public class participacion_informe{
    private int id_participacion;
    private int id_informe;
    private int id_usuario;

    public participacion_informe() {
    }

    public participacion_informe(int id_informe, int id_usuario) {
        this.id_informe = id_informe;
        this.id_usuario = id_usuario;
    }

    public int getId_participacion() {
        return id_participacion;
    }

    public void setId_participacion(int id_participacion) {
        this.id_participacion = id_participacion;
    }

    public int getId_informe() {
        return id_informe;
    }

    public void setId_informe(int id_informe) {
        this.id_informe = id_informe;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    @Override
    public String toString() {
        return "participacion_informe{" + "id_participacion=" + id_participacion + ", id_informe=" + id_informe + ", id_usuario=" + id_usuario + '}';
    }
}
