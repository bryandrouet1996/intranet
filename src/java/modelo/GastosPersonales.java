/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Timestamp;

/**
 *
 * @author USUARIO
 */
public class GastosPersonales {
    private int id;
    private usuario usuario;
    private int estado;
    private double c103;
    private double c104;
    private double c105;
    private double c106;
    private double c107;
    private double c108;
    private double c109;
    private double c110;
    private double c111;
    private double c112;
    private double c113;
    private String adjunto;
    private Timestamp creacion;
    private AprobacionGastosPersonales aprobacion = null;
    private ValidacionGastosPersonales validacion = null;
    private RechazoGastosPersonales rechazo = null;    

    public GastosPersonales() {
    }

    public GastosPersonales(int idUsu, double c103, double c104, double c105, double c106, double c107, double c108, double c109, double c110, double c111, double c112, double c113) {
        this.usuario = new usuario();
        this.usuario.setId_usuario(idUsu);
        this.c103 = c103;
        this.c104 = c104;
        this.c105 = c105;
        this.c106 = c106;
        this.c107 = c107;
        this.c108 = c108;
        this.c109 = c109;
        this.c110 = c110;
        this.c111 = c111;
        this.c112 = c112;
        this.c113 = c113;
    }    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(usuario usuario) {
        this.usuario = usuario;
    }   
        
    public double getC103() {
        return c103;
    }

    public void setC103(double c103) {
        this.c103 = c103;
    }

    public double getC104() {
        return c104;
    }

    public void setC104(double c104) {
        this.c104 = c104;
    }

    public double getC105() {
        return c105;
    }

    public void setC105(double c105) {
        this.c105 = c105;
    }

    public double getC106() {
        return c106;
    }

    public void setC106(double c106) {
        this.c106 = c106;
    }

    public double getC107() {
        return c107;
    }

    public void setC107(double c107) {
        this.c107 = c107;
    }

    public double getC108() {
        return c108;
    }

    public void setC108(double c108) {
        this.c108 = c108;
    }

    public double getC109() {
        return c109;
    }

    public void setC109(double c109) {
        this.c109 = c109;
    }

    public double getC110() {
        return c110;
    }

    public void setC110(double c110) {
        this.c110 = c110;
    }
    
    public double getC111() {
        return c111;
    }

    public void setC111(double c111) {
        this.c111 = c111;
    }

    public double getC112() {
        return c112;
    }

    public void setC112(double c112) {
        this.c112 = c112;
    }

    public double getC113() {
        return c113;
    }

    public void setC113(double c113) {
        this.c113 = c113;
    }

    public Timestamp getCreacion() {
        return creacion;
    }

    public void setCreacion(Timestamp creacion) {
        this.creacion = creacion;
    }

    public String getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(String adjunto) {
        this.adjunto = adjunto;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public AprobacionGastosPersonales getAprobacion() {
        return aprobacion;
    }

    public void setAprobacion(AprobacionGastosPersonales aprobacion) {
        this.aprobacion = aprobacion;
    }

    public ValidacionGastosPersonales getValidacion() {
        return validacion;
    }

    public void setValidacion(ValidacionGastosPersonales validacion) {
        this.validacion = validacion;
    }

    public RechazoGastosPersonales getRechazo() {
        return rechazo;
    }

    public void setRechazo(RechazoGastosPersonales rechazo) {
        this.rechazo = rechazo;
    }
    
    
}
