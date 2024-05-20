/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author Don Beta
 */
public class ComponenteImpuesto {
    private int id;
    private int idImpuesto;
    private String componente;

    public ComponenteImpuesto() {
    }

    public ComponenteImpuesto(int id, int idImpuesto, String componente) {
        this.id = id;
        this.idImpuesto = idImpuesto;
        this.componente = componente;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdImpuesto() {
        return idImpuesto;
    }

    public void setIdImpuesto(int idImpuesto) {
        this.idImpuesto = idImpuesto;
    }

    public String getComponente() {
        return componente;
    }

    public void setComponente(String componente) {
        this.componente = componente;
    }
    
    
}
