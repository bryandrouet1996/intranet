/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *
 * @author USUARIO
 */
public class Documento {

    private int id;
    private int tipo;
    private String tipoDes;
    private int de;
    private String deNombre;
    private String deCargo;
    private int tipo_circular;
    private DestinatarioDocumento destinatario;
    private ArrayList<DestinatarioDocumento> ids_destinatarios = new ArrayList<>();
    private String destinatarios;
    private int estado;
    private String codigo;
    private String codigo_temp = "";
    private String para = "";
    private String para_cargo = "";
    private String asunto;
    private String contenido;
    private ArrayList<AnexoDocumento> anexos = new ArrayList<>();
    private boolean firmado;
    private String path;
    private int referencia;
    private String referenciaCodigo;
    private Timestamp creacion;
    private Timestamp enviado;

    public Documento() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getTipoDes() {
        return tipoDes;
    }

    public void setTipoDes(String tipoDes) {
        this.tipoDes = tipoDes;
    }

    public int getDe() {
        return de;
    }

    public void setDe(int de) {
        this.de = de;
    }

    public String getDeNombre() {
        return deNombre;
    }

    public void setDeNombre(String deNombre) {
        this.deNombre = deNombre;
    }

    public String getDeCargo() {
        return deCargo;
    }

    public void setDeCargo(String deCargo) {
        this.deCargo = deCargo;
    }

    public ArrayList<DestinatarioDocumento> getIds_destinatarios() {
        return ids_destinatarios;
    }

    public void setIds_destinatarios(ArrayList<DestinatarioDocumento> ids_destinatarios) {
        this.ids_destinatarios = ids_destinatarios;
    }

    public String getDestinatarios() {
        return destinatarios;
    }

    public void setDestinatarios(String destinatarios) {
        this.destinatarios = destinatarios;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo_temp() {
        return codigo_temp;
    }

    public void setCodigo_temp(String codigo_temp) {
        this.codigo_temp = codigo_temp;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public ArrayList<AnexoDocumento> getAnexos() {
        return anexos;
    }

    public void setAnexos(ArrayList<AnexoDocumento> anexos) {
        this.anexos = anexos;
    }

    public boolean isFirmado() {
        return firmado;
    }

    public void setFirmado(boolean firmado) {
        this.firmado = firmado;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getReferencia() {
        return referencia;
    }

    public void setReferencia(int referencia) {
        this.referencia = referencia;
    }

    public String getReferenciaCodigo() {
        return referenciaCodigo;
    }

    public void setReferenciaCodigo(String referenciaCodigo) {
        this.referenciaCodigo = referenciaCodigo;
    }

    public Timestamp getCreacion() {
        return creacion;
    }

    public void setCreacion(Timestamp creacion) {
        this.creacion = creacion;
    }

    public DestinatarioDocumento getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(DestinatarioDocumento destinatario) {
        this.destinatario = destinatario;
    }

    public Timestamp getEnviado() {
        return enviado;
    }

    public void setEnviado(Timestamp enviado) {
        this.enviado = enviado;
    }

    public int getTipo_circular() {
        return tipo_circular;
    }

    public void setTipo_circular(int tipo_circular) {
        this.tipo_circular = tipo_circular;
    }

    public String getPara() {
        return para;
    }

    public void setPara(String para) {
        this.para = para;
    }
    
    public String getPara_cargo() {
        return para_cargo;
    }

    public void setPara_cargo(String para_cargo) {
        this.para_cargo = para_cargo;
    }
}
