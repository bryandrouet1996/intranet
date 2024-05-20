/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Date;
import java.sql.Time;

/**
 *
 * @author Rayner
 */
public class FichaRiesgos {
    private String codigo;
    private String fecha;
    private String latitud;
    private String longitud;
    private String direccion;
    private String tecnico;
    private int ficha;
    private String hora;
    private String parroquia;
    private String lugar;
    private String barrio;
    private String sector;
    private String zona;
    private String distrito;
    private int distancia;
    private String tiempo;
    private String punRef;
    private String coorX;
    private String coorY;
    private String altitud;
    private String accesibilidad;
    private String fechaIni;
    private String horaIni;
    private String descripcion;
    private String evento;
    private String efectos;
    private String SNGRE;
    private String MIES;
    private String MSP;
    private String MINEDUC;
    private String GADMCE;
    private String bomberos;
    private String PN;
    private String coopInt;
    private String prefectura;
    private String MAGAP;
    private String turismo;
    private String observacion;
    private String adversos;
    private String mVida;
    private String conclusiones;
    private int nViv;
    private int nFam;
    private int nPer;
    private int nEsco;
    private int nAdul;
    private int nDis;
    private int nBono;
    private int nFalle;
    private int nHeri;
    private String acciones;
    private String recomendaciones;

    public FichaRiesgos() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTecnico() {
        return tecnico;
    }

    public void setTecnico(String tecnico) {
        this.tecnico = tecnico;
    }

    public int getFicha() {
        return ficha;
    }

    public void setFicha(int ficha) {
        this.ficha = ficha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getParroquia() {
        return parroquia;
    }

    public void setParroquia(String parroquia) {
        this.parroquia = parroquia;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getPunRef() {
        return punRef;
    }

    public void setPunRef(String punRef) {
        this.punRef = punRef;
    }

    public String getCoorX() {
        return coorX;
    }

    public void setCoorX(String coorX) {
        this.coorX = coorX;
    }

    public String getCoorY() {
        return coorY;
    }

    public void setCoorY(String coorY) {
        this.coorY = coorY;
    }

    public String getAltitud() {
        return altitud;
    }

    public void setAltitud(String altitud) {
        this.altitud = altitud;
    }

    public String getAccesibilidad() {
        return accesibilidad;
    }

    public void setAccesibilidad(String accesibilidad) {
        this.accesibilidad = accesibilidad;
    }

    public String getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(String fechaIni) {
        this.fechaIni = fechaIni;
    }

    public String getHoraIni() {
        return horaIni;
    }

    public void setHoraIni(String horaIni) {
        this.horaIni = horaIni;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public String getEfectos() {
        return efectos;
    }

    public void setEfectos(String efectos) {
        this.efectos = efectos;
    }

    public String getSNGRE() {
        return SNGRE;
    }

    public void setSNGRE(String SNGRE) {
        this.SNGRE = SNGRE;
    }

    public String getMIES() {
        return MIES;
    }

    public void setMIES(String MIES) {
        this.MIES = MIES;
    }

    public String getMSP() {
        return MSP;
    }

    public void setMSP(String MSP) {
        this.MSP = MSP;
    }

    public String getMINEDUC() {
        return MINEDUC;
    }

    public void setMINEDUC(String MINEDUC) {
        this.MINEDUC = MINEDUC;
    }

    public String getGADMCE() {
        return GADMCE;
    }

    public void setGADMCE(String GADMCE) {
        this.GADMCE = GADMCE;
    }

    public String getBomberos() {
        return bomberos;
    }

    public void setBomberos(String bomberos) {
        this.bomberos = bomberos;
    }

    public String getPN() {
        return PN;
    }

    public void setPN(String PN) {
        this.PN = PN;
    }

    public String getCoopInt() {
        return coopInt;
    }

    public void setCoopInt(String coopInt) {
        this.coopInt = coopInt;
    }

    public String getPrefectura() {
        return prefectura;
    }

    public void setPrefectura(String prefectura) {
        this.prefectura = prefectura;
    }

    public String getMAGAP() {
        return MAGAP;
    }

    public void setMAGAP(String MAGAP) {
        this.MAGAP = MAGAP;
    }

    public String getTurismo() {
        return turismo;
    }

    public void setTurismo(String turismo) {
        this.turismo = turismo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getAdversos() {
        return adversos;
    }

    public void setAdversos(String adversos) {
        this.adversos = adversos;
    }

    public String getmVida() {
        return mVida;
    }

    public void setmVida(String mVida) {
        this.mVida = mVida;
    }

    public String getConclusiones() {
        return conclusiones;
    }

    public void setConclusiones(String conclusiones) {
        this.conclusiones = conclusiones;
    }

    public int getnViv() {
        return nViv;
    }

    public void setnViv(int nViv) {
        this.nViv = nViv;
    }

    public int getnFam() {
        return nFam;
    }

    public void setnFam(int nFam) {
        this.nFam = nFam;
    }

    public int getnPer() {
        return nPer;
    }

    public void setnPer(int nPer) {
        this.nPer = nPer;
    }

    public int getnEsco() {
        return nEsco;
    }

    public void setnEsco(int nEsco) {
        this.nEsco = nEsco;
    }

    public int getnAdul() {
        return nAdul;
    }

    public void setnAdul(int nAdul) {
        this.nAdul = nAdul;
    }

    public int getnDis() {
        return nDis;
    }

    public void setnDis(int nDis) {
        this.nDis = nDis;
    }

    public int getnBono() {
        return nBono;
    }

    public void setnBono(int nBono) {
        this.nBono = nBono;
    }

    public int getnFalle() {
        return nFalle;
    }

    public void setnFalle(int nFalle) {
        this.nFalle = nFalle;
    }

    public int getnHeri() {
        return nHeri;
    }

    public void setnHeri(int nHeri) {
        this.nHeri = nHeri;
    }

    public String getAcciones() {
        return acciones;
    }

    public void setAcciones(String acciones) {
        this.acciones = acciones;
    }

    public String getRecomendaciones() {
        return recomendaciones;
    }

    public void setRecomendaciones(String recomendaciones) {
        this.recomendaciones = recomendaciones;
    }

    

    
}
