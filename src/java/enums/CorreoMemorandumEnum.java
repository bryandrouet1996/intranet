/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package enums;

/**
 *
 * @author Bryan Druet
 */
public enum CorreoMemorandumEnum {
    
    TALENTO_HUMANO("bryandrouet96@gmail.com");
    
    private String param;

    private CorreoMemorandumEnum(String param) {
        this.param = param;
    }
    
    public String getParam() {
	return param;
    }
}
