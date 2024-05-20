/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author Luis Torres F
 */
public class inventario {
    private int id_inventario;
private String nombre;
private String usuariodominio;
private String tipodispositivo;
private String macaddress;
private String memoria;
private String procesador;
private String direccion_ip;
private String conexion_dhcp;
private String conexion_permanente;
private String antivirus;
private String cabildo;
private String sigame;
private String office365;
private String arquitectura_so;
private String codigo_bodega;
private String observaciones;
private String nombre_edificio;
private String piso;
private String unidad_administrativa;
private String funcionario;

    public inventario(String nombre, String usuariodominio, String tipodispositivo, String macaddress, String memoria, String procesador, String direccion_ip, String conexion_dhcp, String conexion_permanente, String antivirus, String cabildo, String sigame, String office365, String arquitectura_so, String codigo_bodega, String observaciones, String nombre_edificio, String piso, String unidad_administrativa, String funcionario) {
        this.nombre = nombre;
        this.usuariodominio = usuariodominio;
        this.tipodispositivo = tipodispositivo;
        this.macaddress = macaddress;
        this.memoria = memoria;
        this.procesador = procesador;
        this.direccion_ip = direccion_ip;
        this.conexion_dhcp = conexion_dhcp;
        this.conexion_permanente = conexion_permanente;
        this.antivirus = antivirus;
        this.cabildo = cabildo;
        this.sigame = sigame;
        this.office365 = office365;
        this.arquitectura_so = arquitectura_so;
        this.codigo_bodega = codigo_bodega;
        this.observaciones = observaciones;
        this.nombre_edificio = nombre_edificio;
        this.piso = piso;
        this.unidad_administrativa = unidad_administrativa;
        this.funcionario = funcionario;
    }




    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }


    public String getProcesador() {
        return procesador;
    }

    public void setProcesador(String procesador) {
        this.procesador = procesador;
    }

    public String getDireccion_ip() {
        return direccion_ip;
    }

    public void setDireccion_ip(String direccion_ip) {
        this.direccion_ip = direccion_ip;
    }

    public String getConexion_dhcp() {
        return conexion_dhcp;
    }

    public void setConexion_dhcp(String conexion_dhcp) {
        this.conexion_dhcp = conexion_dhcp;
    }

    public String getConexion_permanente() {
        return conexion_permanente;
    }

    public void setConexion_permanente(String conexion_permanente) {
        this.conexion_permanente = conexion_permanente;
    }

    public String getAntivirus() {
        return antivirus;
    }

    public void setAntivirus(String antivirus) {
        this.antivirus = antivirus;
    }

    public String getCabildo() {
        return cabildo;
    }

    public void setCabildo(String cabildo) {
        this.cabildo = cabildo;
    }

    public String getSigame() {
        return sigame;
    }

    public void setSigame(String sigame) {
        this.sigame = sigame;
    }

    public String getOffice365() {
        return office365;
    }

    public void setOffice365(String office365) {
        this.office365 = office365;
    }

    public String getArquitectura_so() {
        return arquitectura_so;
    }

    public void setArquitectura_so(String arquitectura_so) {
        this.arquitectura_so = arquitectura_so;
    }

    public String getCodigo_bodega() {
        return codigo_bodega;
    }

    public void setCodigo_bodega(String codigo_bodega) {
        this.codigo_bodega = codigo_bodega;
    }


    public String getNombre_edificio() {
        return nombre_edificio;
    }

    public void setNombre_edificio(String nombre_edificio) {
        this.nombre_edificio = nombre_edificio;
    }

    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public String getUnidad_administrativa() {
        return unidad_administrativa;
    }

    public void setUnidad_administrativa(String unidad_administrativa) {
        this.unidad_administrativa = unidad_administrativa;
    }

    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }


    public String getMemoria() {
        return memoria;
    }

    public void setMemoria(String memoria) {
        this.memoria = memoria;
    }

    public inventario(String nombre, String usuariodominio, String tipodispositivo, String macaddress, String memoria) {
        this.nombre = nombre;
        this.usuariodominio = usuariodominio;
        this.tipodispositivo = tipodispositivo;
        this.macaddress = macaddress;
        this.memoria = memoria;
    }

  
    public inventario(int id_inventario, String macaddress) {
        this.id_inventario = id_inventario;
        this.macaddress = macaddress;
    }

    public String getMacaddress() {
        return macaddress;
    }


    public void setMacaddress(String macaddress) {
        this.macaddress = macaddress;
    }

    public inventario() {
    }

    public inventario(int id_inventario, String nombre, String usuariodominio) {
        this.id_inventario = id_inventario;
        this.nombre = nombre;
        this.usuariodominio = usuariodominio;
    }

    public String getTipodispositivo() {
        return tipodispositivo;
    }

    public void setTipodispositivo(String tipodispositivo) {
        this.tipodispositivo = tipodispositivo;
    }

    public inventario(String nombre, String usuariodominio, String tipodispositivo) {
        this.nombre = nombre;
        this.usuariodominio = usuariodominio;
        this.tipodispositivo = tipodispositivo;
    }

    public inventario(String nombre) {
        this.nombre = nombre;
    }

    public inventario(String nombre, String usuariodominio) {
        this.nombre = nombre;
        this.usuariodominio = usuariodominio;
    }


    public String getUsuariodominio() {
        return usuariodominio;
    }

    public void setUsuariodominio(String usuariodominio) {
        this.usuariodominio = usuariodominio;
    }


    public int getId_inventario() {
        return id_inventario;
    }

    public void setId_inventario(int id_inventario) {
        this.id_inventario = id_inventario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

   
}
