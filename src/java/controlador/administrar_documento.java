/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import firmador.ReporteFirma;
import firmador.RunJar;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import modelo.AnexoDocumento;
import modelo.AsignacionDocumento;
import modelo.DestinatarioDocumento;
import modelo.Documento;
import modelo.TipoDocumento;
import modelo.UsuarioConfig;
import modelo.conexion;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.apache.commons.io.FileUtils;

// <editor-fold defaultstate="collapsed" desc="Configuración de TOMCAT para ejecutar JAVA 11">
/*
VERIFICAR VERSIÓN JAVA QUE EJECUTA TOMCAT
ps -ef | grep tomcat
ACTUALIZAR VARIABLES DE ENTORNO
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
export PATH=$PATH:$JAVA_HOME/bin
REINICIAR SERVICIO
sudo /opt/tomcat/apache-tomcat-8.5.45/bin/shutdown.sh
sudo /opt/tomcat/apache-tomcat-8.5.45/bin/startup.sh
 */
// </editor-fold>
@MultipartConfig(maxFileSize = 1024 * 1024 * 2)
@WebServlet(name = "administrar_documento", urlPatterns = {"/administrar_documento.control"})
public class administrar_documento extends HttpServlet {

    private static final String INSTITUCION = "GADMCE";
    public static final String TEMPORAL = "TMP";
    private static final String FORMATO_CODIGO = "%04d";
    private static final String PATH = "/newmedia/gestor_documental/";
//    private static final String PATH = "D:/gestor_documental/";
    private static final boolean CORREO_ENABLED = true;
    private static final String PATH_FIRMADOR = PATH + "FIRMADOR.jar";
    private static final String POSITION_ON_PAGE_LOWER_LEFT_X = "250";
    private static final String POSITION_ON_PAGE_LOWER_LEFT_Y = "70";
    private static final String INFO_QR = "VALIDAR CON: www.firmadigital.gob.ec";
    private static final String JRXML_DOCUMENTO = "/WEB-INF/classes/reporte/Documento.jrxml";
    private static final String JRXML_RUTA = "/WEB-INF/classes/reporte/Ruta.jrxml";
    private static final String PATH_REPORTES = "/WEB-INF/classes/reporte/";
    private static final String COMMENT_REASIGNAR_BORRADOR = "Reasignación de borrador a nuevo usuario";
    private static final String COMMENT_REASIGNAR_RECIBIDO = "Reasignación de documento recibido a nuevo usuario";
    private static final String COMMENT_INFORMAR_RECIBIDO = "Informar documento recibido a nuevo usuario";
    private static final String CORREO_ASUNTO = "NUEVO DOCUMENTO RECIBIDO: ";
    private static final String[] CORREO_CONTENIDO_GENERAL = {"Estimado/a funcionario/a,\n", ".\nIngresa a la Intranet, Módulo Gestor Documental para revisarlo."};
    private static final String[] CORREO_CONTENIDO_ESPECIFICO = {" te ha enviado el documento: ", " te ha reasignado el documento: ", " te ha informado del documento: "};

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    // <editor-fold defaultstate="collapsed" desc="Métodos ya probados">
    private static ReporteFirma getReporteFirma(final int id, final String path, final String path_reportes) throws JRException {
        File archivoReporte = new File(path);
        if (archivoReporte.getPath() == null) {
            System.out.println("No encuentro el archivo del reporte.");
            System.exit(2);
        }
        Map parametro = new HashMap();
        parametro.put("id", id);
        parametro.put("SUBREPORT_DIR", path_reportes);
        JasperDesign jasperDesign = JRXmlLoader.load(archivoReporte.getAbsolutePath());
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametro, conexion.getEnlace());
        return new ReporteFirma(jasperPrint.getPages().size(), JasperExportManager.exportReportToPdf(jasperPrint));
    }

    private static boolean verificarFirma(final String firma, final String clave_firma) {
        final ArrayList<String> args = new ArrayList<>();
        args.add(PATH_FIRMADOR);
        args.add(firma);
        args.add(clave_firma);
        return RunJar.runJar(args);
    }

    private static boolean firmarDoc(final String firma, final String clave_firma, final String archivo, final String pagina, final String nuevo_archivo) {
        final ArrayList<String> args = new ArrayList<>();
        args.add(PATH_FIRMADOR);
        args.add(firma);
        args.add(clave_firma);
        args.add(archivo);
        args.add(pagina);
        args.add(POSITION_ON_PAGE_LOWER_LEFT_X);
        args.add(POSITION_ON_PAGE_LOWER_LEFT_Y);
        args.add(INFO_QR);
        args.add(nuevo_archivo);
        return RunJar.runJar(args);
    }

    private static String getPathAnexo(final AnexoDocumento a, final Part p) {
        return PATH + a.getId() + "_ANEXO_" + p.getSubmittedFileName();
    }

    private static String getPathFirma(final int id_usuario, final Part p) {
        return PATH + id_usuario + "_FIRMA_" + p.getSubmittedFileName();
    }

    private static String getPathReporte(final Documento d) {
        return PATH + d.getId() + "_REPORTE.pdf";
    }

    private static String getPathReporteFirmado(final Documento d) {
        return PATH + d.getCodigo() + ".pdf";
    }

    private static String getNumeroDocumentoTemporal(final conexion mysql, final String prefijo_codigo) {
        final String ultimo_codigo = mysql.getNumeroDocumentoTemporal(prefijo_codigo);
        final int nuevo_numero = ultimo_codigo.equals("") ? 1 : Integer.parseInt(ultimo_codigo.split("-")[3]) + 1;
        return prefijo_codigo + String.format(FORMATO_CODIGO, nuevo_numero) + "-" + TEMPORAL;
    }

    private static String getNumeroDocumento(final conexion mysql, final String prefijo_codigo, final TipoDocumento tipo) {
        final String ultimo_codigo = mysql.getNumeroDocumento(prefijo_codigo, tipo.getId());
        final int nuevo_numero = ultimo_codigo.equals("") ? 1 : Integer.parseInt(ultimo_codigo.split("-")[3]) + 1;
        return prefijo_codigo + String.format(FORMATO_CODIGO, nuevo_numero) + "-" + tipo.getCodigo();
    }

    private static String getPrefijoCodigo(final String codigo_direccion) {
        return INSTITUCION + "-" + codigo_direccion + "-" + LocalDate.now().getYear() + "-";
    }

    private static void enviarCorreo(final conexion mysql, final Documento d, final int indice_contenido) {
        String destinatarios = "";
        for (DestinatarioDocumento dd : d.getIds_destinatarios()) {
            destinatarios += dd.getCorreo_usuario() + ",";
            if (indice_contenido == 0) {
                try {
                    dd.setCreacion(new Timestamp(new Date().getTime()));
                    mysql.actualizarDestinatarioDocumento(dd);
                } catch (Exception ex) {
                    System.out.println("actualizarDestinatarioDocumento | " + ex.getMessage());
                }
            }
        }
        if (CORREO_ENABLED) {
            destinatarios = destinatarios.substring(0, destinatarios.length() - 1);
            final String correo_asunto = CORREO_ASUNTO + d.getCodigo(),
                    correo_contenido = CORREO_CONTENIDO_GENERAL[0] + d.getDeNombre() + CORREO_CONTENIDO_ESPECIFICO[indice_contenido] + d.getCodigo() + CORREO_CONTENIDO_GENERAL[1];
            mysql.enviarCorreoMod(destinatarios, correo_asunto, correo_contenido);
        }
    }
// </editor-fold>

    private static void enviarDoc(final PrintWriter out, final HttpServletRequest request, final conexion mysql, final int id_documento, final int de, final Documento d, final boolean es_borrador, final boolean registrar_des, final boolean actualizar_des, final boolean eliminar_des, final Collection<Part> parts, final String clave_firma) throws Exception {
        if (id_documento != 0) {
            mysql.actualizarDocumento(d);
        } else {
            d.setId(mysql.registrarDocumento(d));
        }
        if (registrar_des) {
            mysql.registrarDestinatariosDocumento(d);
        }
        if (actualizar_des) {
            mysql.actualizarDestinatariosDocumento(d);
        }
        if (eliminar_des) {
            mysql.eliminarDestinatariosDocumento(d);
        }
        String path_firma = "";
        for (Part p : parts) {
            if (p.getName().equals("firma")) {
                path_firma = getPathFirma(de, p);
                p.write(path_firma);
                parts.remove(p);
                break;
            }
        }
        for (Part p : parts) {
            final AnexoDocumento a = new AnexoDocumento();
            a.setIdDocumento(d.getId());
            a.setNombre(p.getSubmittedFileName());
            a.setId(mysql.registrarAnexoDocumento(a));
            final String path = getPathAnexo(a, p);
            p.write(path);
            a.setPath(path);
            mysql.actualizarAnexoDocumento(a);
        }
        if (es_borrador) {
            mysql.confirmCommit();
            out.print("0;Documento guardado, pasará a la bandeja 'Borradores';ges_doc_borradores.jsp");
        } else {
            final String path_reporte = getPathReporte(d),
                    path_reporte_firmado = getPathReporteFirmado(d);
            final ReporteFirma reporte_firma = getReporteFirma(d.getId(), request.getRealPath(JRXML_DOCUMENTO), request.getRealPath(PATH_REPORTES));
            final int ultPagina = reporte_firma.getPagina();
            if (path_firma.equals("")) {
                FileUtils.writeByteArrayToFile(new File(path_reporte_firmado), reporte_firma.getReporte());
                d.setFirmado(false);
                d.setPath(path_reporte_firmado);
                d.setEnviado(new Timestamp(new Date().getTime()));
                mysql.actualizarPathDocumento(d);
                mysql.confirmCommit();
                out.print("0;Documento guardado, pasará a la bandeja 'Por imprimir';ges_doc_por_imprimir.jsp");
            } else {
                final File reporte = new File(path_reporte);
                FileUtils.writeByteArrayToFile(reporte, reporte_firma.getReporte());
                final boolean doc_firmado = firmarDoc(path_firma, clave_firma, path_reporte, Integer.toString(ultPagina), path_reporte_firmado);
                new File(path_firma).delete();
                final String res;
                d.setEnviado(new Timestamp(new Date().getTime()));
                if (doc_firmado) {
                    d.setFirmado(true);
                    res = "1;Documento firmado y enviado;ges_doc_mis_documentos.jsp";
                    d.setIds_destinatarios(mysql.getDestinatariosDocumento(d.getId()));
                    enviarCorreo(mysql, d, 0);
                } else {
                    d.setEstado(1);
                    d.setFirmado(false);
                    mysql.actualizarFirmadoDocumento(d);
                    final ReporteFirma reporte_firma_temp = getReporteFirma(d.getId(), request.getRealPath(JRXML_DOCUMENTO), request.getRealPath(PATH_REPORTES));
                    FileUtils.writeByteArrayToFile(new File(path_reporte_firmado), reporte_firma_temp.getReporte());
                    res = "0;Documento sin firmar, pasará a la bandeja 'Por imprimir';ges_doc_por_imprimir.jsp";
                }
                reporte.delete();
                d.setPath(path_reporte_firmado);
                mysql.actualizarPathDocumento(d);
                mysql.confirmCommit();
                out.print(res);
            }
        }
    }

    private static void construirDoc(final HttpServletRequest request, final PrintWriter out, final conexion mysql, final int tipo_documento, final int de, final int id_documento, final int referencia, final boolean es_borrador) {
        try {
            mysql.setAutoCommit(false);
            final String[] destinatarios = request.getParameter("destinatarios").split(";");
            ArrayList<DestinatarioDocumento> ids_destinatarios = new ArrayList<>(),
                    ids_destinatarios_temp = new ArrayList<>();
            for (String des : destinatarios) {
                final String[] partes = des.split(":");
                if (partes.length != 2) {
                    throw new Exception("Destinatarios inválidos");
                }
                final DestinatarioDocumento desti = new DestinatarioDocumento(Integer.valueOf(partes[0]), Integer.valueOf(partes[1]));
                ids_destinatarios.add(desti);
                ids_destinatarios_temp.add(desti);
            }
            ArrayList<DestinatarioDocumento> destinatarios_temp = new ArrayList<>();
            final TipoDocumento tipo = mysql.getTipoDocumento(tipo_documento);
            final String codigo_direccion = mysql.getCodigoDireccionUsuario(de),
                    prefijo_codigo = getPrefijoCodigo(codigo_direccion),
                    codigo,
                    asunto = request.getParameter("asunto").toUpperCase(),
                    contenido = request.getParameter("contenido"),
                    clave_firma = request.getParameter("clave_firma");
            Collection<Part> parts = request.getParts();
            parts.removeIf((p) -> {
                return p.getSubmittedFileName() == null ? true : p.getSubmittedFileName().equals("");
            });
            File fileDir = new File(PATH);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            Documento d = id_documento != 0 ? mysql.getDocumento(id_documento, de) : new Documento();
            d.setTipo_circular(0);
            d.setPara("");
            d.setPara_cargo("");
            if (es_borrador) {
                d.setEstado(0);
                codigo = id_documento != 0 ? d.getCodigo() : getNumeroDocumentoTemporal(mysql, prefijo_codigo);
                d.setCodigo_temp(codigo);
            } else {
                if (clave_firma.equals("")) {
                    d.setEstado(1);
                } else {
                    d.setEstado(2);
                    d.setFirmado(true);
                }
                codigo = getNumeroDocumento(mysql, prefijo_codigo, tipo);
            }
            d.setTipo(tipo.getId());
            d.setDe(de);
            final UsuarioConfig remitente = mysql.getUser(de);
            d.setDeNombre(remitente.getNombres());
            boolean registrar_des = false,
                    actualizar_des = false,
                    eliminar_des = false;
            if (d.getId() == 0) {
                for (DestinatarioDocumento des : ids_destinatarios) {
                    d.getIds_destinatarios().add(des);
                }
                registrar_des = true;
            } else {
                destinatarios_temp = new ArrayList<>(d.getIds_destinatarios());
                for (DestinatarioDocumento des : ids_destinatarios) {
                    for (int i = 0; i < d.getIds_destinatarios().size(); i++) {
                        final DestinatarioDocumento destinatario = d.getIds_destinatarios().get(i);
                        if (des.getId_usuario() == destinatario.getId_usuario()) {
                            ids_destinatarios_temp.remove(des);
                            destinatarios_temp.remove(destinatario);
                            break;
                        }
                    }
                }
                ids_destinatarios = new ArrayList<>(ids_destinatarios_temp);
                d.setIds_destinatarios(new ArrayList<>(destinatarios_temp));
                for (DestinatarioDocumento des : ids_destinatarios) {
                    for (DestinatarioDocumento destinatario : d.getIds_destinatarios()) {
                        if (!destinatario.isActualizar()) {
                            ids_destinatarios_temp.remove(des);
                            destinatario.setId_usuario(des.getId_usuario());
                            destinatario.setActualizar(true);
                            actualizar_des = true;
                            break;
                        }
                    }
                }
                ids_destinatarios = new ArrayList<>(ids_destinatarios_temp);
                if (ids_destinatarios.isEmpty()) {
                    for (DestinatarioDocumento destinatario : d.getIds_destinatarios()) {
                        if (!destinatario.isActualizar()) {
                            destinatario.setEliminar(true);
                            eliminar_des = true;
                        }
                    }
                } else {
                    for (DestinatarioDocumento des : ids_destinatarios_temp) {
                        d.getIds_destinatarios().add(des);
                        registrar_des = true;
                    }
                }
            }
            d.setCodigo(codigo);
            d.setAsunto(asunto);
            d.setContenido(contenido);
            d.setReferencia(referencia);
            enviarDoc(out, request, mysql, id_documento, de, d, es_borrador, registrar_des, actualizar_des, eliminar_des, parts, clave_firma);
        } catch (Exception ex) {
            System.out.println("registrar | " + ex.getLocalizedMessage());
            mysql.rollback();
            out.print(";Ocurrió un error al registrar el documento (" + ex.getLocalizedMessage() + ")");
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        final String accion = request.getParameter("accion");
        conexion mysql = new conexion();
        final HttpSession sesion = request.getSession();
        if (accion.equalsIgnoreCase("reporte_borrador")) {
            final ServletOutputStream out = response.getOutputStream();
            try {
                final int id_usuario = Integer.parseInt(sesion.getAttribute("user").toString()),
                        id_documento = Integer.parseInt(request.getParameter("id"));
                final Documento d = mysql.getDocumento(id_documento);
                if (d.getId() == 0) {
                    throw new Exception("No existe el borrador que se trata de visualizar");
                }
                if (d.getDe() != id_usuario) {
                    throw new Exception("Sin acceso al borrador que se trata de visualizar");
                }
                try {
                    File archivoReporte = new File(request.getRealPath(JRXML_DOCUMENTO));
                    if (archivoReporte.getPath() == null) {
                        System.out.println("No encuentro el archivo del reporte.");
                        System.exit(2);
                    }
                    Map parametro = new HashMap();
                    parametro.put("id", id_documento);
                    parametro.put("SUBREPORT_DIR", request.getRealPath(PATH_REPORTES));
                    JasperDesign jasperDesign = JRXmlLoader.load(archivoReporte.getAbsolutePath());
                    JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametro, conexion.getEnlace());
                    response.setContentType("application/pdf");
                    JasperExportManager.exportReportToPdfStream(jasperPrint, out);
                    out.flush();
                    out.close();
                } catch (JRException j) {
                    System.out.println("Mensaje de Error:" + j.getMessage());
                    throw new Exception("Error al generar el reporte");
                }
            } catch (Exception ex) {
                System.out.println("reporte_borrador | " + ex);
                out.print(ex.getMessage());
            }
        } else if (accion.contains("ver_documento")) {
            response.setContentType("application/pdf");
            final ServletOutputStream out = response.getOutputStream();
            try {
                final int id_usuario = Integer.parseInt(sesion.getAttribute("user").toString()),
                        id_documento = Integer.parseInt(request.getParameter("id_documento"));
                final Documento d = mysql.getDocumento(id_documento);
                if (d.getId() == 0) {
                    throw new Exception("No existe el documento que se trata de visualizar");
                }
                if (d.getEstado() == 0) {
                    throw new Exception("El documento que quiere visualizar no ha sido generado");
                }
                if (accion.equalsIgnoreCase("ver_documento_creado")) {
                    if (d.getDe() != id_usuario) {
                        throw new Exception("Sin acceso al documento que se trata de visualizar");
                    }
                } else if (accion.equalsIgnoreCase("ver_documento_recibido")) {
                    final DestinatarioDocumento dd = mysql.getDestinatarioDocumento(id_documento, id_usuario);
                    if (dd.getId() == 0 || dd.getEstado() == 0) {
                        throw new Exception("Sin acceso al documento que se trata de visualizar");
                    }
                    if (dd.getLeido() == null) {
                        dd.setLeido(new Timestamp(new Date().getTime()));
                        mysql.actualizarDestinatarioDocumento(dd);
                    }
                } else {
                    throw new Exception("Opción inválida");
                }
                final File file = new File(d.getPath());
                FileInputStream fis = new FileInputStream(file);
                int size = fis.available();
                byte[] data = new byte[size];
                fis.read(data, 0, size);
                fis.close();
                out.write(data);
            } catch (Exception ex) {
                System.out.println("ver_documento | " + ex);
                out.print(ex.getMessage());
            }
        } else if (accion.contains("descargar_documento")) {
            final ServletOutputStream out = response.getOutputStream();
            try {
                final int id_usuario = Integer.parseInt(sesion.getAttribute("user").toString()),
                        id_documento = Integer.parseInt(request.getParameter("id_documento"));
                final Documento d = mysql.getDocumento(id_documento);
                if (d.getId() == 0) {
                    throw new Exception("No existe el documento que se trata de descargar");
                }
                if (d.getEstado() == 0) {
                    throw new Exception("El documento que quiere descargar no ha sido generado");
                }
                if (accion.equalsIgnoreCase("descargar_documento_creado")) {
                    if (d.getDe() != id_usuario) {
                        throw new Exception("Sin acceso al documento que se trata de descargar");
                    }
                } else if (accion.equalsIgnoreCase("descargar_documento_recibido")) {
                    final DestinatarioDocumento dd = mysql.getDestinatarioDocumento(id_documento, id_usuario);
                    if (dd.getId() == 0 || dd.getEstado() == 0) {
                        throw new Exception("Sin acceso al documento que se trata de descargar");
                    }
                    if (dd.getLeido() == null) {
                        dd.setLeido(new Timestamp(new Date().getTime()));
                        mysql.actualizarDestinatarioDocumento(dd);
                    }
                } else {
                    throw new Exception("Opción inválida");
                }
                final File file = new File(d.getPath());
                final FileInputStream fis = new FileInputStream(file);
                final int size = fis.available();
                final byte[] bytes = new byte[size];
                int read = 0;
                response.setHeader("Content-Disposition", "attachment;filename=\"" + d.getCodigo() + ".pdf\"");
                while ((read = fis.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                fis.close();
                out.flush();
                out.close();
            } catch (Exception ex) {
                System.out.println("descargar_documento | " + ex);
                out.print(ex.getMessage());
            }
        } else if (accion.equalsIgnoreCase("ver_mi_hoja_ruta")) {
            final ServletOutputStream out = response.getOutputStream();
            try {
                final int id_usuario = Integer.parseInt(sesion.getAttribute("user").toString()),
                        id_documento = Integer.parseInt(request.getParameter("id_documento"));
                final Documento d = mysql.getDocumento(id_documento);
                if (d.getId() == 0) {
                    throw new Exception("No existe el documento que se trata de visualizar");
                }
                if (d.getEstado() == 0) {
                    throw new Exception("El documento que quiere visualizar no ha sido generado");
                }
                if (d.getDe() != id_usuario) {
                    throw new Exception("Sin acceso al documento que se trata de visualizar");
                }
                try {
                    File archivoReporte = new File(request.getRealPath(JRXML_RUTA));
                    if (archivoReporte.getPath() == null) {
                        System.out.println("No encuentro el archivo del reporte.");
                        System.exit(2);
                    }
                    Map parametro = new HashMap();
                    parametro.put("id_documento", id_documento);
                    final String path = request.getRealPath(PATH_REPORTES);
                    parametro.put("SUBREPORT_DIR", path);
                    JasperDesign jasperDesign = JRXmlLoader.load(archivoReporte.getAbsolutePath());
                    JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametro, conexion.getEnlace());
                    response.setContentType("application/pdf");
                    JasperExportManager.exportReportToPdfStream(jasperPrint, out);
                    out.flush();
                    out.close();
                } catch (JRException j) {
                    System.out.println("Mensaje de Error:" + j.getMessage());
                    throw new Exception("Error al generar el reporte");
                }
            } catch (Exception ex) {
                System.out.println("ver_mi_hoja_ruta | " + ex);
                out.print(ex.getMessage());
            }
        } else {
            final PrintWriter out = response.getWriter();
            if (accion.equalsIgnoreCase("verificar_firma")) {
                final int id_usuario = Integer.parseInt(sesion.getAttribute("user").toString());
                final Part firma = request.getPart("firma");
                final String clave_firma = request.getParameter("clave_firma"),
                        path_firma = getPathFirma(id_usuario, firma);
                File fileDir = new File(PATH);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                firma.write(path_firma);
                final boolean firma_valida = verificarFirma(path_firma, clave_firma);
                new File(path_firma).delete();
                out.print(firma_valida ? "1" : "0");
            } else if (accion.equalsIgnoreCase("registrar")) {
                final int id_documento = Integer.parseInt(request.getParameter("id_documento")),
                        referencia = Integer.parseInt(request.getParameter("referencia")),
                        de = Integer.parseInt(sesion.getAttribute("user").toString()),
                        tipo_documento = Integer.parseInt(request.getParameter("tipo"));
                final boolean es_borrador = request.getParameter("es_borrador").equals("1");
                switch (tipo_documento) {
                    case 1://MEMO
                        construirDoc(request, out, mysql, tipo_documento, de, id_documento, referencia, es_borrador);
                        break;
                    case 2://CIRCULAR
                        try {
                            final int tipo_circular = Integer.parseInt(request.getParameter("tipo_circular"));
                            if (!es_borrador && !(mysql.verificarUsuarioCumpleRol(de, "administrador") || mysql.verificarUsuarioCumpleRol(de, "director"))) {
                                throw new Exception("Sólo los directores pueden enviar circulares");
                            }
                            if (tipo_circular == 3) {
                                //DEFINIR SI LE LLEGA A CADA FUNCIONARIO
                                throw new Exception("Tipo de circular (general) no implementada");
                            }
                            mysql.setAutoCommit(false);
                            final TipoDocumento tipo = mysql.getTipoDocumento(tipo_documento);
                            final String codigo_direccion = mysql.getCodigoDireccionUsuario(de),
                                    prefijo_codigo = getPrefijoCodigo(codigo_direccion),
                                    codigo,
                                    asunto = request.getParameter("asunto").toUpperCase(),
                                    contenido = request.getParameter("contenido"),
                                    clave_firma = request.getParameter("clave_firma");
                            Collection<Part> parts = request.getParts();
                            parts.removeIf((p) -> {
                                return p.getSubmittedFileName() == null ? true : p.getSubmittedFileName().equals("");
                            });
                            File fileDir = new File(PATH);
                            if (!fileDir.exists()) {
                                fileDir.mkdirs();
                            }
                            Documento d = id_documento != 0 ? mysql.getDocumento(id_documento, de) : new Documento();
                            d.setPara("");
                            d.setPara_cargo("");
                            if (!d.getIds_destinatarios().isEmpty()) {
                                for (DestinatarioDocumento des : d.getIds_destinatarios()) {
                                    des.setEliminar(true);
                                }
                                mysql.eliminarDestinatariosDocumento(d);
                            }
                            if (es_borrador) {
                                d.setEstado(0);
                                codigo = id_documento != 0 ? d.getCodigo() : getNumeroDocumentoTemporal(mysql, prefijo_codigo);
                                d.setCodigo_temp(codigo);
                            } else {
                                if (clave_firma.equals("")) {
                                    d.setEstado(1);
                                } else {
                                    d.setEstado(2);
                                    d.setFirmado(true);
                                }
                                codigo = getNumeroDocumento(mysql, prefijo_codigo, tipo);
                            }
                            d.setTipo(tipo.getId());
                            d.setDe(de);
                            d.setTipo_circular(tipo_circular);
                            d.setCodigo(codigo);
                            d.setAsunto(asunto);
                            d.setContenido(contenido);
                            d.setReferencia(referencia);
                            final boolean registrar_des = !es_borrador;
                            if (registrar_des) {//SÓLO SE GUARDAN DESTINATARIOS AL ENVIAR
                                final UsuarioConfig remitente = mysql.getUser(de);
                                ArrayList<UsuarioConfig> destinatarios;
                                switch (tipo_circular) {
                                    case 1:
                                        destinatarios = mysql.getUsersDireccion(remitente);
                                        break;
                                    case 2:
                                        destinatarios = mysql.getUsersDirectores(remitente);
                                        break;
                                    default:
                                        throw new Exception("Tipo de circular no implementada");
                                }
                                for (UsuarioConfig u : destinatarios) {
                                    d.getIds_destinatarios().add(
                                            new DestinatarioDocumento(u.getId())
                                    );
                                }
                            }
                            enviarDoc(out, request, mysql, id_documento, de, d, es_borrador, registrar_des, false, false, parts, clave_firma);
                        } catch (Exception ex) {
                            System.out.println("registrar_circular | " + ex.getLocalizedMessage());
                            mysql.rollback();
                            out.print(";" + ex.getMessage());
                        }
                        break;
                    case 3://OFICIO
                        try {
                            mysql.setAutoCommit(false);
                            final String destinatario = request.getParameter("para").toUpperCase(),
                                    cargo_destinatario = request.getParameter("cargo_para").toUpperCase();
                            final TipoDocumento tipo = mysql.getTipoDocumento(tipo_documento);
                            final String codigo_direccion = mysql.getCodigoDireccionUsuario(de),
                                    prefijo_codigo = getPrefijoCodigo(codigo_direccion),
                                    codigo,
                                    asunto = request.getParameter("asunto").toUpperCase(),
                                    contenido = request.getParameter("contenido"),
                                    clave_firma = request.getParameter("clave_firma");
                            Collection<Part> parts = request.getParts();
                            parts.removeIf((p) -> {
                                return p.getSubmittedFileName() == null ? true : p.getSubmittedFileName().equals("");
                            });
                            File fileDir = new File(PATH);
                            if (!fileDir.exists()) {
                                fileDir.mkdirs();
                            }
                            Documento d = id_documento != 0 ? mysql.getDocumento(id_documento, de) : new Documento();
                            d.setTipo_circular(0);
                            if (!d.getIds_destinatarios().isEmpty()) {
                                for (DestinatarioDocumento des : d.getIds_destinatarios()) {
                                    des.setEliminar(true);
                                }
                                mysql.eliminarDestinatariosDocumento(d);
                            }
                            if (es_borrador) {
                                d.setEstado(0);
                                codigo = id_documento != 0 ? d.getCodigo() : getNumeroDocumentoTemporal(mysql, prefijo_codigo);
                                d.setCodigo_temp(codigo);
                            } else {
                                if (clave_firma.equals("")) {
                                    d.setEstado(1);
                                } else {
                                    d.setEstado(2);
                                    d.setFirmado(true);
                                }
                                codigo = getNumeroDocumento(mysql, prefijo_codigo, tipo);
                            }
                            d.setTipo(tipo.getId());
                            d.setDe(de);
                            d.setCodigo(codigo);
                            d.setPara(destinatario);
                            d.setPara_cargo(cargo_destinatario);
                            d.setAsunto(asunto);
                            d.setContenido(contenido);
                            d.setReferencia(referencia);
                            enviarDoc(out, request, mysql, id_documento, de, d, es_borrador, false, false, false, parts, clave_firma);
                        } catch (Exception ex) {
                            System.out.println("registrar_oficio | " + ex.getMessage());
                            mysql.rollback();
                            out.print(";" + ex);
                        }
                        break;
                    case 4://INFORME
                        construirDoc(request, out, mysql, tipo_documento, de, id_documento, referencia, es_borrador);
                        break;
                    default:
                        try {
                            throw new Exception("Tipo de documento inválido");
                        } catch (Exception ex) {
                            System.out.println("tipo_invalido | " + ex);
                            out.print(";" + ex.getMessage());
                        }
                        break;
                }
            } else if (accion.equalsIgnoreCase("eliminar_anexo")) {
                try {
                    int de = Integer.parseInt(sesion.getAttribute("user").toString()),
                            id_anexo = Integer.parseInt(request.getParameter("id_anexo")),
                            id_documento = Integer.parseInt(request.getParameter("id_documento"));
                    final Documento d = mysql.getDocumento(id_documento, de);
                    if (d.getId() == 0) {
                        throw new Exception("Documento inexistente");
                    }
                    AnexoDocumento anexo = null;
                    for (AnexoDocumento a : d.getAnexos()) {
                        if (id_anexo == a.getId()) {
                            anexo = a;
                            new File(a.getPath()).delete();
                            mysql.eliminarAnexoDocumento(id_anexo);
                            break;
                        }
                    }
                    if (anexo == null) {
                        throw new Exception("Anexo no encontrado");
                    }
                    out.print("1;Anexo eliminado;");
                } catch (Exception ex) {
                    System.out.println("eliminar_anexo | " + ex);
                    out.print(";" + ex.getMessage() + ";");
                }
            } else if (accion.equalsIgnoreCase("eliminar_borrador")) {
                try {
                    int de = Integer.parseInt(sesion.getAttribute("user").toString()),
                            id_documento = Integer.parseInt(request.getParameter("id_documento"));
                    final Documento d = mysql.getDocumento(id_documento, de);
                    if (d.getId() == 0) {
                        throw new Exception("Documento inexistente");
                    }
                    if (d.getEstado() != 0) {
                        throw new Exception("Sólo se pueden eliminar borradores");
                    }
                    for (AnexoDocumento a : d.getAnexos()) {
                        try {
                            new File(a.getPath()).delete();
                        } catch (Exception e) {
                            System.out.println("delete_borrador | " + e.getMessage());
                        }
                    }
                    mysql.eliminarBorrador(id_documento);
                    out.print("1;Borrador eliminado;ges_doc_borradores.jsp");
                } catch (Exception ex) {
                    System.out.println("eliminar_borrador | " + ex);
                    out.print(";" + ex.getMessage() + ";");
                }
            } else if (accion.equalsIgnoreCase("reasignar_borrador")) {
                try {
                    int actual = Integer.parseInt(sesion.getAttribute("user").toString()),
                            id_documento = Integer.parseInt(request.getParameter("id_documento")),
                            nuevo_remitente = Integer.parseInt(request.getParameter("nuevo_remitente"));
                    Documento d = mysql.getDocumento(id_documento, actual);
                    if (d.getId() == 0) {
                        throw new Exception("Documento inexistente");
                    }
                    if (d.getEstado() != 0) {
                        throw new Exception("Con esta opción sólo se pueden reasignar borradores");
                    }
                    mysql.setAutoCommit(false);
                    mysql.actualizarRemitenteDocumento(d, nuevo_remitente);
                    final AsignacionDocumento a = new AsignacionDocumento();
                    a.setIdDocumento(id_documento);
                    a.setActual(actual);
                    a.setNuevo(nuevo_remitente);
                    a.setComentario(COMMENT_REASIGNAR_BORRADOR);
                    mysql.registrarAsignacionDocumento(a);
                    mysql.confirmCommit();
                    if (CORREO_ENABLED) {
                        final UsuarioConfig destinatario = mysql.getUser(nuevo_remitente);
                        final String correo_asunto = CORREO_ASUNTO + d.getCodigo(),
                                correo_contenido = CORREO_CONTENIDO_GENERAL[0] + d.getDeNombre() + CORREO_CONTENIDO_ESPECIFICO[1] + d.getCodigo() + CORREO_CONTENIDO_GENERAL[1];
                        mysql.enviarCorreoMod(destinatario.getCorreo(), correo_asunto, correo_contenido);
                    }
                    out.print("1;Documento reasignado;ges_doc_borradores.jsp");
                } catch (Exception ex) {
                    mysql.rollback();
                    System.out.println("reasignar_borrador | " + ex);
                    out.print(";" + ex.getMessage() + ";");
                }
            } else if (accion.equalsIgnoreCase("subir_documento")) {
                final int id_documento = Integer.parseInt(request.getParameter("m_sub_doc_id")),
                        de = Integer.parseInt(sesion.getAttribute("user").toString());
                Collection<Part> parts = request.getParts();
                parts.removeIf((p) -> {
                    return p.getSubmittedFileName() == null ? true : p.getSubmittedFileName().equals("");
                });
                try {
                    final Documento d = mysql.getDocumento(id_documento, de);
                    File fileDir = new File(PATH);
                    if (!fileDir.exists()) {
                        fileDir.mkdirs();
                    }
                    for (Part p : parts) {
                        if (p.getName().equals("m_sub_doc_adjunto")) {
                            p.write(d.getPath());
                            break;
                        }
                    }
                    d.setEstado(2);
                    mysql.actualizarEstadoDocumentoDe(d);
                    enviarCorreo(mysql, d, 0);
                    out.print("1;Documento subido y enviado;ges_doc_mis_documentos.jsp");
                } catch (Exception e) {
                    System.out.println("subir_documento | " + e);
                    out.print(";Ocurrió un error al subir y enviar el documento");
                }
            } else if (accion.equalsIgnoreCase("reasignar_recibido")) {
                try {
                    int actual = Integer.parseInt(sesion.getAttribute("user").toString()),
                            id_documento = Integer.parseInt(request.getParameter("id_documento")),
                            nuevo_destinatario = Integer.parseInt(request.getParameter("nuevo_destinatario"));
                    final Documento d = mysql.getDocumento(id_documento);
                    if (d.getId() == 0) {
                        throw new Exception("Documento inexistente");
                    }
                    if (d.getEstado() != 2) {
                        throw new Exception("Con esta opción sólo se pueden reasignar documentos recibidos");
                    }
                    final DestinatarioDocumento dd_o = mysql.getDestinatarioDocumento(id_documento, actual);
                    if (dd_o.getId() == 0 || dd_o.getEstado() == 0) {
                        throw new Exception("Sin acceso al documento que se trata de reasignar");
                    }
                    if (dd_o.getTipo() == 4) {
                        throw new Exception("Las personas informadas del documento no pueden reasignarlo");
                    }
                    mysql.setAutoCommit(false);
                    dd_o.setEstado(0);
                    mysql.actualizarDestinatarioDocumento(dd_o);
                    DestinatarioDocumento n_dd = mysql.getDestinatarioDocumento(id_documento, nuevo_destinatario);
                    if (n_dd.getId() != 0) {
                        if (n_dd.getTipo() == 4) {
                            throw new Exception("El usuario seleccionado ya fue informado del documento");
                        } else if (n_dd.getEstado() != 0) {
                            throw new Exception("El usuario seleccionado ya tiene una asignación del documento");
                        } else {
                            n_dd.setEstado(1);
                            mysql.actualizarDestinatarioDocumento(n_dd);
                        }
                    } else {
                        n_dd = new DestinatarioDocumento(id_documento, nuevo_destinatario, 3);
                        mysql.registrarDestinatarioDocumento(n_dd);
                    }
                    final AsignacionDocumento a = new AsignacionDocumento();
                    a.setIdDocumento(id_documento);
                    a.setActual(actual);
                    a.setNuevo(nuevo_destinatario);
                    a.setComentario(COMMENT_REASIGNAR_RECIBIDO);
                    mysql.registrarAsignacionDocumento(a);
                    mysql.confirmCommit();
                    enviarCorreo(mysql, d, 1);
                    out.print("1;Documento reasignado;ges_doc_documentos_recibidos.jsp");
                } catch (Exception ex) {
                    mysql.rollback();
                    System.out.println("reasignar_recibido | " + ex);
                    out.print(";" + ex.getMessage() + ";");
                }
            } else if (accion.equalsIgnoreCase("informar_recibido")) {
                try {
                    int actual = Integer.parseInt(sesion.getAttribute("user").toString()),
                            id_documento = Integer.parseInt(request.getParameter("id_documento")),
                            nuevo_destinatario = Integer.parseInt(request.getParameter("nuevo_destinatario"));
                    final Documento d = mysql.getDocumento(id_documento);
                    if (d.getId() == 0) {
                        throw new Exception("Documento inexistente");
                    }
                    if (d.getEstado() != 2) {
                        throw new Exception("Con esta opción sólo se pueden informar documentos recibidos");
                    }
                    final DestinatarioDocumento dd_o = mysql.getDestinatarioDocumento(id_documento, actual);
                    if (dd_o.getId() == 0 || dd_o.getEstado() == 0) {
                        throw new Exception("Sin acceso al documento que se trata de informar");
                    }
                    if (dd_o.getTipo() == 4) {
                        throw new Exception("Las personas informadas del documento no pueden volver a informarlo");
                    }
                    DestinatarioDocumento n_dd = mysql.getDestinatarioDocumento(id_documento, nuevo_destinatario);
                    if (n_dd.getId() != 0) {
                        throw new Exception("El usuario seleccionado ya " + (n_dd.getTipo() == 4 ? "fue informado del documento" : "tuvo una asignación del documento"));
                    }
                    mysql.setAutoCommit(false);
                    n_dd = new DestinatarioDocumento(id_documento, nuevo_destinatario, 4);
                    mysql.registrarDestinatarioDocumento(n_dd);
                    final AsignacionDocumento a = new AsignacionDocumento();
                    a.setIdDocumento(id_documento);
                    a.setActual(actual);
                    a.setNuevo(nuevo_destinatario);
                    a.setComentario(COMMENT_INFORMAR_RECIBIDO);
                    mysql.registrarAsignacionDocumento(a);
                    mysql.confirmCommit();
                    enviarCorreo(mysql, d, 2);
                    out.print("1;Documento informado;ges_doc_documentos_recibidos.jsp");
                } catch (Exception ex) {
                    mysql.rollback();
                    System.out.println("informar_recibido | " + ex);
                    out.print(";" + ex.getMessage() + ";");
                }
            } else if (accion.equalsIgnoreCase("archivar_recibido")) {
                try {
                    int actual = Integer.parseInt(sesion.getAttribute("user").toString()),
                            id_documento = Integer.parseInt(request.getParameter("id_documento"));
                    final Documento d = mysql.getDocumento(id_documento);
                    if (d.getId() == 0) {
                        throw new Exception("Documento inexistente");
                    }
                    if (d.getEstado() != 2) {
                        throw new Exception("Con esta opción sólo se pueden archivar documentos recibidos");
                    }
                    final DestinatarioDocumento dd = mysql.getDestinatarioDocumento(id_documento, actual);
                    if (dd.getId() == 0 || dd.getEstado() == 0) {
                        throw new Exception("Sin acceso al documento que se trata de archivar");
                    }
                    if (dd.getEstado() == 2) {
                        throw new Exception("El documento ya fue archivado");
                    }
                    dd.setEstado(2);
                    mysql.actualizarDestinatarioDocumento(dd);
                    out.print("1;Documento archivado;ges_doc_documentos_recibidos.jsp");
                } catch (Exception ex) {
                    System.out.println("archivar_recibido | " + ex);
                    out.print(";" + ex.getMessage() + ";");
                }
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
