/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package firmador;

/**
 *
 * @author USUARIO
 */
public class ReporteFirma {
    private int pagina;
    private byte[] reporte;

    public ReporteFirma() {
    }

    public ReporteFirma(int pagina, byte[] reporte) {
        this.pagina = pagina;
        this.reporte = reporte;
    }

    public int getPagina() {
        return pagina;
    }

    public void setPagina(int pagina) {
        this.pagina = pagina;
    }

    public byte[] getReporte() {
        return reporte;
    }

    public void setReporte(byte[] reporte) {
        this.reporte = reporte;
    }
    
}
