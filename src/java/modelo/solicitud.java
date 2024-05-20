/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author JC
 */
public class solicitud {
    private int id_solicitud;
    private String cod_solicitud;
    private int id_usuario;
    private int id_servicio;
    private Timestamp fecha_solicitud;
    private int estado;
    private String comentario;
    private String observacion;
    private int funcionario;
    private int paso;
    private int id_rol;
    private int funcionarioant;
    private Timestamp fecha_reenvio;

    public solicitud() {
    }

    public solicitud(int estado, String observacion, int paso) {
        this.estado = estado;
        this.observacion = observacion;
        this.paso = paso;
    }

    public solicitud(int id_usuario, int id_servicio, int estado) {
        this.id_usuario = id_usuario;
        this.id_servicio = id_servicio;
        this.estado = estado;
    }

    public solicitud(int id_usuario, int id_servicio, int estado, String comentario, int id_rol) {
        this.id_usuario = id_usuario;
        this.id_servicio = id_servicio;
        this.estado = estado;
        this.comentario = comentario;
        this.id_rol = id_rol;
    }

    public solicitud(int id_solicitud, String cod_solicitud, int id_usuario, int id_servicio, Timestamp fecha_solicitud, int estado, String comentario, String observacion, int funcionario, int paso, int id_rol, int funcionarioant, Timestamp fecha_reenvio) {
        this.id_solicitud = id_solicitud;
        this.cod_solicitud = cod_solicitud;
        this.id_usuario = id_usuario;
        this.id_servicio = id_servicio;
        this.fecha_solicitud = fecha_solicitud;
        this.estado = estado;
        this.comentario = comentario;
        this.observacion = observacion;
        this.funcionario = funcionario;
        this.paso = paso;
        this.id_rol = id_rol;
        this.funcionarioant = funcionarioant;
        this.fecha_reenvio = fecha_reenvio;
    }

    public solicitud(String cod_solicitud, int id_usuario, int id_servicio, Timestamp fecha_solicitud, int estado, String comentario, String observacion, int funcionario, int paso, int id_rol, int funcionarioant) {
        this.cod_solicitud = cod_solicitud;
        this.id_usuario = id_usuario;
        this.id_servicio = id_servicio;
        this.fecha_solicitud = fecha_solicitud;
        this.estado = estado;
        this.comentario = comentario;
        this.observacion = observacion;
        this.funcionario = funcionario;
        this.paso = paso;
        this.id_rol = id_rol;
        this.funcionarioant = funcionarioant;
    }

    public int getId_solicitud() {
        return id_solicitud;
    }

    public void setId_solicitud(int id_solicitud) {
        this.id_solicitud = id_solicitud;
    }

    public String getCod_solicitud() {
        return cod_solicitud;
    }

    public void setCod_solicitud(String cod_solicitud) {
        this.cod_solicitud = cod_solicitud;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_servicio() {
        return id_servicio;
    }

    public void setId_servicio(int id_servicio) {
        this.id_servicio = id_servicio;
    }

    public Timestamp getFecha_solicitud() {
        return fecha_solicitud;
    }

    public void setFecha_solicitud(Timestamp fecha_solicitud) {
        this.fecha_solicitud = fecha_solicitud;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public int getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(int funcionario) {
        this.funcionario = funcionario;
    }

    public int getPaso() {
        return paso;
    }

    public void setPaso(int paso) {
        this.paso = paso;
    }

    public int getId_rol() {
        return id_rol;
    }

    public void setId_rol(int id_rol) {
        this.id_rol = id_rol;
    }

    public int getFuncionarioant() {
        return funcionarioant;
    }

    public void setFuncionarioant(int funcionarioant) {
        this.funcionarioant = funcionarioant;
    }

    public Timestamp getFecha_reenvio() {
        return fecha_reenvio;
    }

    public void setFecha_reenvio(Timestamp fecha_reenvio) {
        this.fecha_reenvio = fecha_reenvio;
    }
    
    
    
}
