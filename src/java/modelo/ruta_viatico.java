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
public class ruta_viatico{
    private int id_ruta;
    private int id_viatico;
    private int id_lugarpartida;
    private int id_lugarllegada;
    private String tipo_transporte;
    private String nombre_transporte;
    private java.sql.Date fecha_salida;
    private String hora_salida;
    private java.sql.Date fecha_llegada;
    private String hora_llegada;

    public ruta_viatico() {
    }

    public ruta_viatico(int id_viatico, int id_lugarpartida, int id_lugarllegada, String tipo_transporte, String nombre_transporte, java.sql.Date fecha_salida, String hora_salida, java.sql.Date fecha_llegada, String hora_llegada) {
        this.id_viatico = id_viatico;
        this.id_lugarpartida = id_lugarpartida;
        this.id_lugarllegada = id_lugarllegada;
        this.tipo_transporte = tipo_transporte;
        this.nombre_transporte = nombre_transporte;
        this.fecha_salida = fecha_salida;
        this.hora_salida = hora_salida;
        this.fecha_llegada = fecha_llegada;
        this.hora_llegada = hora_llegada;
    }

    public int getId_ruta() {
        return id_ruta;
    }

    public void setId_ruta(int id_ruta) {
        this.id_ruta = id_ruta;
    }

    public int getId_viatico() {
        return id_viatico;
    }

    public void setId_viatico(int id_viatico) {
        this.id_viatico = id_viatico;
    }

    public int getId_lugarpartida() {
        return id_lugarpartida;
    }

    public void setId_lugarpartida(int id_lugarpartida) {
        this.id_lugarpartida = id_lugarpartida;
    }

    public int getId_lugarllegada() {
        return id_lugarllegada;
    }

    public void setId_lugarllegada(int id_lugarllegada) {
        this.id_lugarllegada = id_lugarllegada;
    }

    public String getTipo_transporte() {
        return tipo_transporte;
    }

    public void setTipo_transporte(String tipo_transporte) {
        this.tipo_transporte = tipo_transporte;
    }

    public String getNombre_transporte() {
        return nombre_transporte;
    }

    public void setNombre_transporte(String nombre_transporte) {
        this.nombre_transporte = nombre_transporte;
    }

    public java.sql.Date getFecha_salida() {
        return fecha_salida;
    }

    public void setFecha_salida(java.sql.Date fecha_salida) {
        this.fecha_salida = fecha_salida;
    }

    public String getHora_salida() {
        return hora_salida;
    }

    public void setHora_salida(String hora_salida) {
        this.hora_salida = hora_salida;
    }

    public java.sql.Date getFecha_llegada() {
        return fecha_llegada;
    }

    public void setFecha_llegada(java.sql.Date fecha_llegada) {
        this.fecha_llegada = fecha_llegada;
    }

    public String getHora_llegada() {
        return hora_llegada;
    }

    public void setHora_llegada(String hora_llegada) {
        this.hora_llegada = hora_llegada;
    }

    @Override
    public String toString() {
        return "ruta_viatico{" + "id_ruta=" + id_ruta + ", id_viatico=" + id_viatico + ", id_lugarpartida=" + id_lugarpartida + ", id_lugarllegada=" + id_lugarllegada + ", tipo_transporte=" + tipo_transporte + ", nombre_transporte=" + nombre_transporte + ", fecha_salida=" + fecha_salida + ", hora_salida=" + hora_salida + ", fecha_llegada=" + fecha_llegada + ", hora_llegada=" + hora_llegada + '}';
    }
    
    
}
