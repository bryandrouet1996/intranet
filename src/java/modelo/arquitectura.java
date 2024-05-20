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
public class arquitectura {
    private int id_arquitecturaso;
    private String arquitectura_so;

    public arquitectura(int id_arquitecturaso, String arquitectura_so) {
        this.id_arquitecturaso = id_arquitecturaso;
        this.arquitectura_so = arquitectura_so;
    }

    public int getId_arquitecturaso() {
        return id_arquitecturaso;
    }

    public void setId_arquitecturaso(int id_arquitecturaso) {
        this.id_arquitecturaso = id_arquitecturaso;
    }

    public String getArquitectura_so() {
        return arquitectura_so;
    }

    public void setArquitectura_so(String arquitectura_so) {
        this.arquitectura_so = arquitectura_so;
    }
    public arquitectura() {
    }
}
