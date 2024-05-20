/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinarp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 *
 * @author Don Beta
 */
public class SINARP {

    private static final String BASE_ENDPOINT = "http://186.46.57.100/sinarp/webresources/ciudadano/";//PRODUCCIÓN
//    private static final String BASE_ENDPOINT = "http://127.0.0.1:8080/sinarp/webresources/ciudadano/";//PRUEBA
    private URL url;
    private HttpURLConnection conn;

    public SINARP() {
    }

    private String getOutputString(final String endpoint) throws MalformedURLException, IOException {
        url = new URL(endpoint);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Basic aW50cmFuZXQ6Uldye2J5MiYtZSEtMmFsUQ==");
        conn.setRequestProperty("Accept", "application/json");
        InputStreamReader in = new InputStreamReader(conn.getInputStream(), "UTF-8");
        BufferedReader br = new BufferedReader(in);
        String outputString = "";
        String output;
        while ((output = br.readLine()) != null) {
            outputString += output;
        }
        conn.disconnect();
        return outputString;
    }

    public Ciudadano getRCD(String cedula, boolean actualizar) {
        try {
            final String endpoint = BASE_ENDPOINT + "rcd?" + (actualizar ? "actualizar=true&" : "") + "cedula=" + cedula;
            final String outputString = getOutputString(endpoint);
            Gson gson = new Gson();
            java.lang.reflect.Type ciudadanoType = new TypeToken<Ciudadano>() {
            }.getType();
            return gson.fromJson(outputString, ciudadanoType);
        } catch (Exception e) {
            System.out.println("getRCD | " + e);
            return null;
        }
    }

    public ArrayList<Cedula> getRC(final String apellido1, final String apellido2, final String nombre1, final String nombre2) {
        try {
            final String endpoint = BASE_ENDPOINT + "rc?apellido1=" + apellido1 + (apellido2.equals("") ? "" : ("&apellido2=" + apellido2)) + (nombre1.equals("") ? "" : ("&nombre1=" + nombre1)) + (nombre2.equals("") ? "" : ("&nombre2=" + nombre2));
            final String outputString = getOutputString(endpoint);
            Gson gson = new Gson();
            java.lang.reflect.Type arrayCedsType = new TypeToken<ArrayList<Cedula>>() {
            }.getType();
            return gson.fromJson(outputString, arrayCedsType);
        } catch (Exception e) {
            System.out.println("getRC | " + e);
            return null;
        }
    }

    public Ciudadano getMSP(final String cedula, final boolean actualizar) {
        try {
            final String endpoint = BASE_ENDPOINT + "msp?" + (actualizar ? "actualizar=true&" : "") + "cedula=" + cedula;
            final String outputString = getOutputString(endpoint);
            Gson gson = new Gson();
            java.lang.reflect.Type ciudadanoType = new TypeToken<Ciudadano>() {
            }.getType();
            return gson.fromJson(outputString, ciudadanoType);
        } catch (Exception e) {
            System.out.println("getMSP | " + e);
            return null;
        }
    }
}
