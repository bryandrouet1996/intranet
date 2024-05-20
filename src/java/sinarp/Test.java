/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinarp;

/**
 *
 * @author USUARIO
 */
public class Test {

    public static void main(String[] args) {
        final SINARP s = new SINARP();
        final Ciudadano c = s.getRCD("0202030227", false);
        System.out.println("Nombre: " + c.getNombres());
    }
}
