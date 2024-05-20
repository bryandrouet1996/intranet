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
 * @author Don Beta
 */
public class Capacitacion {
    private int id;
    private int idUsuario;
    private int estado;
    private String usuario;
    private int horarios;
    private Date fechaIni;
    private Date fechaFin;
    private String horaIni;
    private String horaFin;
    private String tema;
    private String descripcion;
    private String adjunto;
    private String informe;
    private int inscritos;
    private int asistentes;
    private int idHorario;
    private double satisfaccion;
    private String satisfaccionDesc;
    private int satisfaccionNum;
    private int asistencia;
    private int satisfaccionAsistente;
    private String satisfaccionAsistenteDesc;  
    private Timestamp satisfaccionAsistenteFec;
    private String enlace;

    public Capacitacion() {
    }   

    public Capacitacion(int id, int idUsuario, int estado, String usuario, Date fechaIni, Date fechaFin, String tema, String descripcion, String adjunto, String informe, int inscritos, int asistentes, double satisfaccion, String satisfaccionDesc, int satisfaccionNum, String enlace) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.estado = estado;
        this.usuario = usuario;
        this.fechaIni = fechaIni;
        this.fechaFin = fechaFin;
        this.tema = tema;
        this.descripcion = descripcion;
        this.adjunto = adjunto;
        this.informe = informe;
        this.inscritos = inscritos;
        this.asistentes = asistentes;
        this.satisfaccion = satisfaccion;
        this.satisfaccionDesc = satisfaccionDesc;
        this.satisfaccionNum = satisfaccionNum;
        this.enlace = enlace;
    }     
                
    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Date getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(String adjunto) {
        this.adjunto = adjunto;
    }

    public String getHoraIni() {
        return horaIni;
    }

    public void setHoraIni(String horaIni) {
        this.horaIni = horaIni;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getHorarios() {
        return horarios;
    }

    public void setHorarios(int horarios) {
        this.horarios = horarios;
    }

    public double getSatisfaccion() {
        return satisfaccion;
    }

    public void setSatisfaccion(double satisfaccion) {
        this.satisfaccion = satisfaccion;
    }   
    
    public int getSatisfaccionNum() {
        return satisfaccionNum;
    }

    public void setSatisfaccionNum(int satisfaccionNum) {
        this.satisfaccionNum = satisfaccionNum;
    }

    public int getSatisfaccionAsistente() {
        return satisfaccionAsistente;
    }

    public void setSatisfaccionAsistente(int satisfaccionAsistente) {
        this.satisfaccionAsistente = satisfaccionAsistente;
    }

    public String getSatisfaccionAsistenteDesc() {
        return satisfaccionAsistenteDesc;
    }

    public void setSatisfaccionAsistenteDesc(String satisfaccionAsistenteDesc) {
        this.satisfaccionAsistenteDesc = satisfaccionAsistenteDesc;
    }

    public int getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(int idHorario) {
        this.idHorario = idHorario;
    }

    public int getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(int asistencia) {
        this.asistencia = asistencia;
    }

    public Timestamp getSatisfaccionAsistenteFec() {
        return satisfaccionAsistenteFec;
    }

    public void setSatisfaccionAsistenteFec(Timestamp satisfaccionAsistenteFec) {
        this.satisfaccionAsistenteFec = satisfaccionAsistenteFec;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getInscritos() {
        return inscritos;
    }

    public void setInscritos(int inscritos) {
        this.inscritos = inscritos;
    }

    public int getAsistentes() {
        return asistentes;
    }

    public void setAsistentes(int asistentes) {
        this.asistentes = asistentes;
    }

    public String getInforme() {
        return informe;
    }

    public void setInforme(String informe) {
        this.informe = informe;
    }

    public String getSatisfaccionDesc() {
        return satisfaccionDesc;
    }

    public void setSatisfaccionDesc(String satisfaccionDesc) {
        this.satisfaccionDesc = satisfaccionDesc;
    }

    public String getEnlace() {
        return enlace;
    }

    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }
   

    
}
