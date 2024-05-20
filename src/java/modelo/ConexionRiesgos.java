/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *
 * @author DR-PC
 */
public class ConexionRiesgos {

    private static Connection enlace = null;
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String usuario = "root";
    private static final String contrasena = "Servidor2019*";
//    private static final String url = "jdbc:mysql://192.168.130.17:3306/riesgos";//PRUEBA
    private static final String url = "jdbc:mysql://192.168.130.16:3306/riesgos";//PRODUCCIÓN
    private ResultSet rs;
    private PreparedStatement st;

    public ConexionRiesgos() {
        try {
            Class.forName(driver);
            enlace = DriverManager.getConnection(url, usuario, contrasena);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex);
        }
    }

    public boolean registrarPrincipal(FichaRiesgos f) throws SQLException {
        String sentencia = "INSERT INTO ficha(COD_FIC, FECHA_FIC, LAT_FIC, LON_FIC, DIR_FIC, TECNICO_FIC, FICHA_FIC, HORA_FIC, PARROQUIA_FIC, LUGAR_FIC, BARRIO_FIC, SECTOR_FIC, ZONA_FIC, DISTRITO_FIC, DISTANCIA_FIC, TIEMPO_FIC, PUNREF_FIC, COORX_FIC, COORY_FIC, ALTITUD_FIC, ACCESI_FIC, FECHAINI_FIC, HORAINI_FIC, DESCRIP_FIC, EVENTO_FIC, EFECTOS_FIC, SNGRE_FIC, MIES_FIC, MSP_FIC, MINEDUC_FIC, GADMCE_FIC, BOMB_FIC, PN_FIC, COOPINT_FIC, PREFECTURA_FIC, MAGAP_FIC, TURIS_FIC, OBS_FIC, ADVERSOS_FIC, MVIDA_FIC, CONCLUS_FIC, NVIV_FIC, NFAM_FIC, NPER_FIC, NESCO_FIC, NADUL_FIC, NDIS_FIC, NBONO_FIC, NFALLE_FIC, NHERI_FIC, ACCIONES_FIC, RECOMEN_FIC) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        st = enlace.prepareStatement(sentencia);
        st.setString(1, f.getCodigo());
        st.setString(2, f.getFecha());
        st.setString(3, f.getLatitud());
        st.setString(4, f.getLongitud());
        st.setString(5, f.getDireccion());
        st.setString(6, f.getTecnico());
        st.setInt(7, f.getFicha());
        st.setString(8, f.getHora());
        st.setString(9, f.getParroquia());
        st.setString(10, f.getLugar());
        st.setString(11, f.getBarrio());
        st.setString(12, f.getSector());
        st.setString(13, f.getZona());
        st.setString(14, f.getDistrito());
        st.setInt(15, f.getDistancia());
        st.setString(16, f.getTiempo());
        st.setString(17, f.getPunRef());
        st.setString(18, f.getCoorX());
        st.setString(19, f.getCoorY());
        st.setString(20, f.getAltitud());
        st.setString(21, f.getAccesibilidad());
        st.setString(22, f.getFechaIni());
        st.setString(23, f.getHoraIni());
        st.setString(24, f.getDescripcion());
        st.setString(25, f.getEvento());
        st.setString(26, f.getEfectos());
        st.setString(27, f.getSNGRE());
        st.setString(28, f.getMIES());
        st.setString(29, f.getMSP());
        st.setString(30, f.getMINEDUC());
        st.setString(31, f.getGADMCE());
        st.setString(32, f.getBomberos());
        st.setString(33, f.getPN());
        st.setString(34, f.getCoopInt());
        st.setString(35, f.getPrefectura());
        st.setString(36, f.getMAGAP());
        st.setString(37, f.getTurismo());
        st.setString(38, f.getObservacion());
        st.setString(39, f.getAdversos());
        st.setString(40, f.getmVida());
        st.setString(41, f.getConclusiones());
        st.setInt(42, f.getnViv());
        st.setInt(43, f.getnFam());
        st.setInt(44, f.getnPer());
        st.setInt(45, f.getnEsco());
        st.setInt(46, f.getnAdul());
        st.setInt(47, f.getnDis());
        st.setInt(48, f.getnBono());
        st.setInt(49, f.getnFalle());
        st.setInt(50, f.getnHeri());
        st.setString(51, f.getAcciones());
        st.setString(52, f.getRecomendaciones());
        st.executeUpdate();
        st.close();
        return true;
    }

    public boolean registrarCiudadano(PersonaRiesgos c) throws SQLException {
        String sentencia = "INSERT INTO ciudadano(CED_CIU, COD_FIC, NOM_CIU, EDAD_CIU, TELF_CIU, OCUP_CIU, BONO_CIU, DISC_CIU, ESTUD_CIU) VALUES(?,?,?,?,?,?,?,?,?)";
        st = enlace.prepareStatement(sentencia);
        st.setString(1, c.getCedula());
        st.setString(2, c.getCodFicha());        
        st.setString(3, c.getNombre());
        st.setInt(4, c.getEdad());        
        st.setString(5, c.getTelefono());
        st.setString(6, c.getOcupacion());
        st.setBoolean(7, c.isBono());
        st.setBoolean(8, c.isDiscapacidad());
        st.setBoolean(9, c.isEstudiante());
        st.executeUpdate();
        st.close();
        return true;
    }

    public ArrayList<FichaRiesgos> getFichas() {
        ArrayList<FichaRiesgos> res = new ArrayList();
        try {
            String sentencia = "SELECT * FROM ficha";
            st = enlace.prepareStatement(sentencia);
            rs = st.executeQuery();
            while (rs.next()) {
                FichaRiesgos ficha = new FichaRiesgos();
                ficha.setCodigo(rs.getString("COD_FIC"));
                ficha.setFecha(rs.getString("FECHA_FIC"));
                ficha.setTecnico(rs.getString("TECNICO_FIC"));
                ficha.setParroquia(rs.getString("PARROQUIA_FIC"));
                ficha.setBarrio(rs.getString("BARRIO_FIC"));
                ficha.setSector(rs.getString("SECTOR_FIC"));
                res.add(ficha);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getFichas | " + ex);
        }
        return res;
    }

    public ArrayList<FichaRiesgos> getFichasUsuarioFecha(int idUsu, Date inicio, Date fin) {
        ArrayList<FichaRiesgos> res = new ArrayList();
        try {
            String sentencia = "SELECT * FROM ficha WHERE CAST(FECHA_FIC AS DATE) BETWEEN ? AND ?";
            st = enlace.prepareStatement(sentencia);
            st.setDate(1, inicio);
            st.setDate(2, fin);
            rs = st.executeQuery();
            while (rs.next()) {
                FichaRiesgos ficha = new FichaRiesgos();
                ficha.setCodigo(rs.getString("COD_FIC"));
                ficha.setFecha(rs.getString("FECHA_FIC"));
                ficha.setTecnico(rs.getString("TECNICO_FIC"));
                ficha.setParroquia(rs.getString("PARROQUIA_FIC"));
                ficha.setBarrio(rs.getString("BARRIO_FIC"));
                ficha.setSector(rs.getString("SECTOR_FIC"));
                res.add(ficha);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getFichasUsuarioFecha | " + ex);
        }
        return res;
    }

    public ArrayList<FichaRiesgos> getFichasFecha(Date inicio, Date fin) {
        ArrayList<FichaRiesgos> res = new ArrayList();
        try {
            String sentencia = "SELECT * FROM ficha WHERE CAST(FECHA_FIC AS DATE) BETWEEN ? AND ?";
            st = enlace.prepareStatement(sentencia);
            st.setDate(1, inicio);
            st.setDate(2, fin);
            rs = st.executeQuery();
            while (rs.next()) {
                FichaRiesgos ficha = new FichaRiesgos();
                ficha.setCodigo(rs.getString("COD_FIC"));
                ficha.setFecha(rs.getString("FECHA_FIC"));
                ficha.setTecnico(rs.getString("TECNICO_FIC"));
                ficha.setParroquia(rs.getString("PARROQUIA_FIC"));
                ficha.setBarrio(rs.getString("BARRIO_FIC"));
                ficha.setSector(rs.getString("SECTOR_FIC"));
                res.add(ficha);
            }
            st.close();
            rs.close();
        } catch (SQLException ex) {
            System.out.println("getFichasFecha | " + ex);
        }
        return res;
    }

}
