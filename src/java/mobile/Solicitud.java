/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobile;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *
 * @author User
 */
public class Solicitud {

    private int id;
    private int tipo;
    private String tipoDes;
    private EstadoSolicitud estado = new EstadoSolicitud();
    private CategoriaSolicitud categoria = new CategoriaSolicitud();
    private SubcategoriaSolicitud subcategoria = new SubcategoriaSolicitud();
    private String cedula;
    private String nombre;
    private String correo;
    private String celular;
    private String direccion;
    private String descripcion;
    private String observacion;
    private Timestamp creacion;
    private Timestamp cierre;
    private int gestiona;
    private ArrayList<ArchivoSolicitud> archivos = new ArrayList<>();

    public Solicitud() {
    }

    public Solicitud(int tipo, int subcategoria, String cedula, String nombre, String correo, String celular, String direccion, String descripcion) {
        this.tipo = tipo;
        this.subcategoria.setId(subcategoria);
        this.cedula = cedula;
        this.nombre = nombre;
        this.correo = correo;
        this.celular = celular;
        this.direccion = direccion;
        this.descripcion = descripcion;
    }

    public Solicitud(int id, int tipo, String tipoDes, int estado, String estadoDes, int categoria, String categoriaDes, int subcategoria, String subcategoriaDes, String cedula, String nombre, String correo, String celular, String direccion, String descripcion, String observacion, Timestamp creacion, Timestamp cierre, int gestiona) {
        this.id = id;
        this.tipo = tipo;
        this.tipoDes = tipoDes;
        this.estado.setId(estado);
        this.estado.setNombre(estadoDes);
        this.categoria.setId(categoria);
        this.categoria.setNombre(categoriaDes);
        this.subcategoria.setId(subcategoria);
        this.subcategoria.setNombre(subcategoriaDes);
        this.cedula = cedula;
        this.nombre = nombre;
        this.correo = correo;
        this.celular = celular;
        this.direccion = direccion;
        this.descripcion = descripcion;
        this.observacion = observacion;
        this.creacion = creacion;
        this.cierre = cierre;
        this.gestiona = gestiona;
    }

    public int getGestiona() {
        return gestiona;
    }

    public void setGestiona(int gestiona) {
        this.gestiona = gestiona;
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

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }

    public CategoriaSolicitud getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaSolicitud categoria) {
        this.categoria = categoria;
    }

    public SubcategoriaSolicitud getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(SubcategoriaSolicitud subcategoria) {
        this.subcategoria = subcategoria;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Timestamp getCreacion() {
        return creacion;
    }

    public void setCreacion(Timestamp creacion) {
        this.creacion = creacion;
    }

    public Timestamp getCierre() {
        return cierre;
    }

    public void setCierre(Timestamp cierre) {
        this.cierre = cierre;
    }

    public ArrayList<ArchivoSolicitud> getArchivos() {
        return archivos;
    }

    public void setArchivos(ArrayList<ArchivoSolicitud> archivos) {
        this.archivos = archivos;
    }

}
