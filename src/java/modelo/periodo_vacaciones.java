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
public class periodo_vacaciones {
    private int id_periodo;
    private int anio;
    private int dias_disponibles;

    public periodo_vacaciones() {
    }

    public periodo_vacaciones(int anio, int dias_disponibles) {
        this.anio = anio;
        this.dias_disponibles = dias_disponibles;
    }

    public int getId_periodo() {
        return id_periodo;
    }

    public void setId_periodo(int id_periodo) {
        this.id_periodo = id_periodo;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getDias_disponibles() {
        return dias_disponibles;
    }

    public void setDias_disponibles(int dias_disponibles) {
        this.dias_disponibles = dias_disponibles;
    }

    @Override
    public String toString() {
        return "periodo_vacaciones{" + "id_periodo=" + id_periodo + ", anio=" + anio + ", dias_disponibles=" + dias_disponibles + '}';
    }
   
}
