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
 * @author Kevin Druet
 */
public class tramite {
    private int id_tramite;
    private String numero_memorando;
    private Date fecha_elaboracion;
    private String hora_elaboracion;
    private Timestamp fecha_recepcion;
    private int id_envia;
    private int id_para;
    private String asunto;
    private int tipo_tramite;
    private int subtipo;
    private int id_estado;
    private String hash;
    private String devuelto;

    public tramite(int id_tramite, String numero_memorando, Date fecha_elaboracion, String hora_elaboracion, Timestamp fecha_recepcion, int id_envia, int id_para, String asunto, int tipo_tramite, int subtipo, int id_estado, String adjunto) {
        this.id_tramite = id_tramite;
        this.numero_memorando = numero_memorando;
        this.fecha_elaboracion = fecha_elaboracion;
        this.hora_elaboracion = hora_elaboracion;
        this.fecha_recepcion = fecha_recepcion;
        this.id_envia = id_envia;
        this.id_para = id_para;
        this.asunto = asunto;
        this.tipo_tramite = tipo_tramite;
        this.subtipo = subtipo;
        this.id_estado = id_estado;
        this.adjunto = adjunto;
    }
    private String adjunto;
    private Timestamp fecha_creacion;
    private Timestamp fecha_update;

    public tramite() {
    }

    public tramite(String numero_memorando, Date fecha_elaboracion, String hora_elaboracion, int id_envia, int id_para, String asunto, int tipo_tramite) {
        this.numero_memorando = numero_memorando;
        this.fecha_elaboracion = fecha_elaboracion;
        this.hora_elaboracion = hora_elaboracion;
        this.id_envia = id_envia;
        this.id_para = id_para;
        this.asunto = asunto;
        this.tipo_tramite = tipo_tramite;
    }
   
    public int getId_tramite() {
        return id_tramite;
    }

    public void setId_tramite(int id_tramite) {
        this.id_tramite = id_tramite;
    }

    public String getNumero_memorando() {
        return numero_memorando;
    }

    public void setNumero_memorando(String numero_memorando) {
        this.numero_memorando = numero_memorando;
    }

    public Date getFecha_elaboracion() {
        return fecha_elaboracion;
    }

    public void setFecha_elaboracion(Date fecha_elaboracion) {
        this.fecha_elaboracion = fecha_elaboracion;
    }

    public String getHora_elaboracion() {
        return hora_elaboracion;
    }

    public void setHora_elaboracion(String hora_elaboracion) {
        this.hora_elaboracion = hora_elaboracion;
    }
    
    public Timestamp getFecha_recepcion() {
        return fecha_recepcion;
    }

    public void setFecha_recepcion(Timestamp fecha_recepcion) {
        this.fecha_recepcion = fecha_recepcion;
    }

    public int getId_envia() {
        return id_envia;
    }

    public void setId_envia(int id_envia) {
        this.id_envia = id_envia;
    }

    public int getId_para() {
        return id_para;
    }

    public void setId_para(int id_para) {
        this.id_para = id_para;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public int getTipo_tramite() {
        return tipo_tramite;
    }

    public void setTipo_tramite(int tipo_tramite) {
        this.tipo_tramite = tipo_tramite;
    }

    public int getSubtipo() {
        return subtipo;
    }

    public void setSubtipo(int subtipo) {
        this.subtipo = subtipo;
    }

    public int getId_estado() {
        return id_estado;
    }

    public void setId_estado(int id_estado) {
        this.id_estado = id_estado;
    }

    public String getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(String adjunto) {
        this.adjunto = adjunto;
    }

    public Timestamp getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Timestamp fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public Timestamp getFecha_update() {
        return fecha_update;
    }

    public void setFecha_update(Timestamp fecha_update) {
        this.fecha_update = fecha_update;
    }   
    
    @Override
    public String toString() {
        return "tramite{" + "id_tramite=" + id_tramite + ", numero_memorando=" + numero_memorando + ", fecha_elaboracion=" + fecha_elaboracion + ", fecha_recepcion=" + fecha_recepcion + ", id_envia=" + id_envia + ", id_para=" + id_para + ", asunto=" + asunto + ", tipo_tramite=" + tipo_tramite + ", subtipo=" + subtipo + ", id_estado=" + id_estado + ", adjunto=" + adjunto + ", fecha_creacion=" + fecha_creacion + ", fecha_update=" + fecha_update + '}';
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getDevuelto() {
        return devuelto;
    }

    public void setDevuelto(String devuelto) {
        this.devuelto = devuelto;
    }
    
    
}
