/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilidades;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bryan Druet
 */
public class Utility {

    public Utility() {
    }

    public java.sql.Date ConvertStringToDate(String date) {
        java.sql.Date convertDate = null;
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date dateToConvert = sdf.parse(date);
            convertDate = new java.sql.Date(dateToConvert.getTime());
        } catch (ParseException ex) {
            Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return convertDate;
    }
    
    public java.sql.Date fecha_actual(String date) {
        java.sql.Date convertDate = null;
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date dateToConvert = sdf.parse(date);
            convertDate = new java.sql.Date(dateToConvert.getTime());
        } catch (ParseException ex) {
            Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return convertDate;
    }

}
