/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobile;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author USUARIO
 */
public class API {

    private static final String BASE_ENDPOINT = "http://192.168.120.16:3060/mobileapp-api/v1/";//PRODUCCIÓN
//    private static final String BASE_ENDPOINT = "http://localhost:3060/mobileapp-api/v1/";//PRUEBA
    private URL url;
    private HttpURLConnection conn;
    private String outputString;

    public API() {

    }

    public boolean atenderSolicitud(Solicitud s) {
        try {
            final String endpoint = BASE_ENDPOINT + "solicitudes/atenderUnsecure";
            url = new URL(endpoint);
            conn = (HttpURLConnection) url.openConnection();
            Gson gson = new Gson();
            final String jsonText = gson.toJson(
                    new SolicitudApi(
                            s.getId(),
                            s.getEstado().getId(),
                            s.getObservacion()
                    )
            );
            byte[] buffer = jsonText.getBytes("UTF-8");
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json;");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStream out = conn.getOutputStream();
            out.write(buffer);
            out.close();
            InputStreamReader in = new InputStreamReader(conn.getInputStream(), "UTF-8");
            BufferedReader br = new BufferedReader(in);
            String output;
            while ((output = br.readLine()) != null) {
                outputString = output;
            }
            conn.disconnect();
            return true;
        } catch (Exception e) {
            System.out.println("atenderSolicitud | " + e);
            return false;
        }
    }
}
