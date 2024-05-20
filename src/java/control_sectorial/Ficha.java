/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control_sectorial;

/**
 *
 * @author USUARIO
 */
public class Ficha {

    private int id;
    private int id_barrio;
    private Barrio barrio = new Barrio();
    private int id_creador;
    private String nombre_creador;
    private int id_sector;
    private Sector sector = new Sector();
    private int id_parroquia;
    private Parroquia parroquia = new Parroquia();
    private boolean visita_alcalde;
    private boolean brigada_medica;
    private boolean brigada_veterinaria;
    private boolean olla_solidaria;
    private boolean minga_ciudadana;
    private boolean capacitacion;
    private boolean wifi;
    private int servicios_entregados;

    public int getServicios_entregados() {
        return servicios_entregados;
    }

    public void setServicios_entregados(int servicios_entregados) {
        this.servicios_entregados = servicios_entregados;
    }

    public Ficha() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_barrio() {
        return id_barrio;
    }

    public void setId_barrio(int id_barrio) {
        this.id_barrio = id_barrio;
    }

    public Barrio getBarrio() {
        return barrio;
    }

    public void setBarrio(Barrio barrio) {
        this.barrio = barrio;
    }

    public int getId_creador() {
        return id_creador;
    }

    public void setId_creador(int id_creador) {
        this.id_creador = id_creador;
    }

    public String getNombre_creador() {
        return nombre_creador;
    }

    public void setNombre_creador(String nombre_creador) {
        this.nombre_creador = nombre_creador;
    }

    public int getId_sector() {
        return id_sector;
    }

    public void setId_sector(int id_sector) {
        this.id_sector = id_sector;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public int getId_parroquia() {
        return id_parroquia;
    }

    public void setId_parroquia(int id_parroquia) {
        this.id_parroquia = id_parroquia;
    }

    public Parroquia getParroquia() {
        return parroquia;
    }

    public void setParroquia(Parroquia parroquia) {
        this.parroquia = parroquia;
    }

    public boolean isVisita_alcalde() {
        return visita_alcalde;
    }

    public void setVisita_alcalde(boolean visita_alcalde) {
        this.visita_alcalde = visita_alcalde;
    }

    public boolean isBrigada_medica() {
        return brigada_medica;
    }

    public void setBrigada_medica(boolean brigada_medica) {
        this.brigada_medica = brigada_medica;
    }

    public boolean isBrigada_veterinaria() {
        return brigada_veterinaria;
    }

    public void setBrigada_veterinaria(boolean brigada_veterinaria) {
        this.brigada_veterinaria = brigada_veterinaria;
    }

    public boolean isOlla_solidaria() {
        return olla_solidaria;
    }

    public void setOlla_solidaria(boolean olla_solidaria) {
        this.olla_solidaria = olla_solidaria;
    }

    public boolean isMinga_ciudadana() {
        return minga_ciudadana;
    }

    public void setMinga_ciudadana(boolean minga_ciudadana) {
        this.minga_ciudadana = minga_ciudadana;
    }

    public boolean isCapacitacion() {
        return capacitacion;
    }

    public void setCapacitacion(boolean capacitacion) {
        this.capacitacion = capacitacion;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public String stringify() {
        if (id == 0) {
            return "{}";
        } else {
            return "{\"id\":" + id
                    + ",\"id_barrio\":" + id_barrio
                    + ",\"id_creador\":" + id_creador
                    + ",\"visita_alcalde\":" + visita_alcalde
                    + ",\"brigada_medica\":" + brigada_medica
                    + ",\"brigada_veterinaria\":" + brigada_veterinaria
                    + ",\"olla_solidaria\":" + olla_solidaria
                    + ",\"minga_ciudadana\":" + minga_ciudadana
                    + ",\"capacitacion\":" + capacitacion
                    + ",\"wifi\":" + wifi + "}";
        }
    }
}
