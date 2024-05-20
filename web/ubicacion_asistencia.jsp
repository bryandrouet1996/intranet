<%-- 
    Document   : ubicacion_asistencia
    Created on : 30/03/2021, 15:37:08
    Author     : Kevin Druet
--%>

<%@page import="modelo.asistencia"%>
<%@page import="modelo.conexion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    conexion enlace = new conexion();
    int id_asistencia = 0;
    asistencia elemento = null;
    try {
        if (request.getParameter("id_asistencia") != null) {
            id_asistencia = Integer.parseInt(request.getParameter("id_asistencia"));
        }
        elemento = enlace.obtenerAsistenciaId(id_asistencia);
    } catch (Exception e) {
        System.out.println(e);
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <style> 
            #mapid {
                height: 300px;
            }
            html, body {
                height: 450px;
                margin: 0;
                padding: 0;
            }
        </style> 
        <link href="assets/modules/leaflet/leaflet.css" rel="stylesheet" type="text/css"/>
        <script src="assets/modules/leaflet/leaflet.js" type="text/javascript"></script>
    </head>
    <body>
        <div class="form-row">
            <div class="col-md-12">
                <div id="mapid"></div>
                <script>
                    var mymap = L.map('mapid').setView([<%= elemento.getLatitud() %>, <%= elemento.getLongitud() %>], 18);
                    L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
                        maxZoom: 25,
                        id: 'mapbox/streets-v11',
                        tileSize: 512,
                        zoomOffset: -1,
                    }).addTo(mymap);
                    L.marker([<%= elemento.getLatitud() %>, <%= elemento.getLongitud() %>]).addTo(mymap);
                </script>
            </div>
        </div>
    </body>
</html>
