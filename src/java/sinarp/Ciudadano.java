package sinarp;

public class Ciudadano {

    private int idAuditoria;
    private int idRegistro;
    //RCD
    private String cedula;
    private String nombres;
    private String fechaNacimiento;
    private String estadoCivil;
    private String fechaExpedicion;
    private String fechaDefuncion;
    private int actaDefuncion;
    //MSP
    private String codigoCONADIS;
    private String grado;
    private int porcentaje;

    public Ciudadano() {
    }

    public Ciudadano(String cedula) {
        this.cedula = cedula;
    }

    public Ciudadano(String cedula, String nombres, String fechaNacimiento, String estadoCivil, String fechaExpedicion, int actaDefuncion) {
        this.cedula = cedula;
        this.nombres = nombres;
        this.fechaNacimiento = fechaNacimiento;
        this.estadoCivil = estadoCivil;
        this.fechaExpedicion = fechaExpedicion;
        this.actaDefuncion = actaDefuncion;
    }

    public Ciudadano(int idRegistro, String cedula, String nombres, String fechaNacimiento, String estadoCivil, String fechaExpedicion, String fechaDefuncion, int actaDefuncion) {
        this.idRegistro = idRegistro;
        this.cedula = cedula;
        this.nombres = nombres;
        this.fechaNacimiento = fechaNacimiento;
        this.estadoCivil = estadoCivil;
        this.fechaExpedicion = fechaExpedicion;
        this.fechaDefuncion = fechaDefuncion;
        this.actaDefuncion = actaDefuncion;
    }

    public Ciudadano(String cedula, String codigoCONADIS, String grado, int porcentaje) {
        this.cedula = cedula;
        this.codigoCONADIS = codigoCONADIS;
        this.grado = grado;
        this.porcentaje = porcentaje;
    }

    public Ciudadano(int idRegistro, String cedula, String codigoCONADIS, String grado, int porcentaje) {
        this.idRegistro = idRegistro;
        this.cedula = cedula;
        this.codigoCONADIS = codigoCONADIS;
        this.grado = grado;
        this.porcentaje = porcentaje;
    }

    public Ciudadano(String cedula, String nombres, String fechaNacimiento, String estadoCivil, String fechaExpedicion, String fechaDefuncion, int actaDefuncion, String codigoCONADIS, String grado, int porcentaje) {
        this.cedula = cedula;
        this.nombres = nombres;
        this.fechaNacimiento = fechaNacimiento;
        this.estadoCivil = estadoCivil;
        this.fechaExpedicion = fechaExpedicion;
        this.fechaDefuncion = fechaDefuncion;
        this.actaDefuncion = actaDefuncion;
        this.codigoCONADIS = codigoCONADIS;
        this.grado = grado;
        this.porcentaje = porcentaje;
    }

    public int getIdAuditoria() {
        return idAuditoria;
    }

    public void setIdAuditoria(int idAuditoria) {
        this.idAuditoria = idAuditoria;
    }

    public int getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(int idRegistro) {
        this.idRegistro = idRegistro;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getFechaExpedicion() {
        return fechaExpedicion;
    }

    public void setFechaExpedicion(String fechaExpedicion) {
        this.fechaExpedicion = fechaExpedicion;
    }

    public String getFechaDefuncion() {
        return fechaDefuncion;
    }

    public void setFechaDefuncion(String fechaDefuncion) {
        this.fechaDefuncion = fechaDefuncion;
    }

    public int getActaDefuncion() {
        return actaDefuncion;
    }

    public void setActaDefuncion(int actaDefuncion) {
        this.actaDefuncion = actaDefuncion;
    }

    public String getCodigoCONADIS() {
        return codigoCONADIS;
    }

    public void setCodigoCONADIS(String codigoCONADIS) {
        this.codigoCONADIS = codigoCONADIS;
    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    public int getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(int porcentaje) {
        this.porcentaje = porcentaje;
    }

}
