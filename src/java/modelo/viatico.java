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
public class viatico {
    private int id_viatico;
    private int id_tipo;
    private int id_usuario;
    private String descripcion_actividad;
    private String tipo_cuenta;
    private String numero_cuenta;
    private String nombre_banco;
    private int id_estado;
    private java.sql.Timestamp fecha;

    public viatico() {
    }

    public viatico(int id_tipo, int id_usuario, String descripcion_actividad, String tipo_cuenta, String numero_cuenta, String nombre_banco) {
        this.id_tipo = id_tipo;
        this.id_usuario = id_usuario;
        this.descripcion_actividad = descripcion_actividad;
        this.tipo_cuenta = tipo_cuenta;
        this.numero_cuenta = numero_cuenta;
        this.nombre_banco = nombre_banco;
    }

    public int getId_viatico() {
        return id_viatico;
    }

    public void setId_viatico(int id_viatico) {
        this.id_viatico = id_viatico;
    }

    public int getId_tipo() {
        return id_tipo;
    }

    public void setId_tipo(int id_tipo) {
        this.id_tipo = id_tipo;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public java.sql.Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(java.sql.Timestamp fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion_actividad() {
        return descripcion_actividad;
    }

    public void setDescripcion_actividad(String descripcion_actividad) {
        this.descripcion_actividad = descripcion_actividad;
    }

    public String getTipo_cuenta() {
        return tipo_cuenta;
    }

    public void setTipo_cuenta(String tipo_cuenta) {
        this.tipo_cuenta = tipo_cuenta;
    }

    public String getNumero_cuenta() {
        return numero_cuenta;
    }

    public void setNumero_cuenta(String numero_cuenta) {
        this.numero_cuenta = numero_cuenta;
    }

    public String getNombre_banco() {
        return nombre_banco;
    }

    public void setNombre_banco(String nombre_banco) {
        this.nombre_banco = nombre_banco;
    }

    public int getId_estado() {
        return id_estado;
    }

    public void setId_estado(int id_estado) {
        this.id_estado = id_estado;
    }
    
}
