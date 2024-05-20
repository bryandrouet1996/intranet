/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package enums;

/**
 *
 * @author Bryan Druet
 */
public enum StatusEnum {
    
    //MEMORANDUMS
    PENDIENTE("PENDIENTE"),
    CONTESTADO("CONTESTADO"),
    COMPLETADO("COMPLETADO");
    
    private String param;

    private StatusEnum(String param) {
        this.param = param;
    }
    
    public String getParam() {
	return param;
    }
}
