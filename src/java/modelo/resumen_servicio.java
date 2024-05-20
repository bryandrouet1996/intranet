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
public class resumen_servicio {
    private int id_resumen;
    private String nombre_mes;
    private int cantidad;

    public resumen_servicio() {
    }

    public resumen_servicio(int id_resumen, String nombre_mes, int cantidad) {
        this.id_resumen = id_resumen;
        this.nombre_mes = nombre_mes;
        this.cantidad = cantidad;
    }

    public int getId_resumen() {
        return id_resumen;
    }

    public void setId_resumen(int id_resumen) {
        this.id_resumen = id_resumen;
    }

    public String getNombre_mes() {
        return nombre_mes;
    }

    public void setNombre_mes(String nombre_mes) {
        this.nombre_mes = nombre_mes;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "resumen_servicio{" + "id_resumen=" + id_resumen + ", nombre_mes=" + nombre_mes + ", cantidad=" + cantidad + '}';
    }
    
}
