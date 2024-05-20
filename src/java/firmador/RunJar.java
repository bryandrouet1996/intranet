/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firmador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author USUARIO
 */
public class RunJar {

    private static String getExecutionLog(int exitVal, BufferedReader errorBuffer, BufferedReader op) {
        String error = "";
        String line;
        try {
            while ((line = errorBuffer.readLine()) != null) {
                error = error + "\n" + line;
            }
        } catch (final IOException e) {
            System.out.println("error ex | " + e);
        }
        String output = "";
        try {
            while ((line = op.readLine()) != null) {
                output = output + "\n" + line;
            }
        } catch (final IOException e) {
            System.out.println("output ex | " + e);
        }
        try {
            errorBuffer.close();
            op.close();
        } catch (final IOException e) {
            System.out.println("error close ex | " + e);
        }
        return "exitVal: " + exitVal + ", error: " + error + ", output: " + output;
    }

    public static boolean runJar(final ArrayList<String> args) {
        try {
            final ArrayList<String> actualArgs = new ArrayList<>();
            actualArgs.add(0, "java");
            actualArgs.add(1, "-jar");
            actualArgs.addAll(args);
            final Runtime re = Runtime.getRuntime();
            final Process command = re.exec(actualArgs.toArray(new String[0]));
            BufferedReader error = new BufferedReader(new InputStreamReader(command.getErrorStream()));
            BufferedReader op = new BufferedReader(new InputStreamReader(command.getInputStream()));
            command.waitFor();
            int exitVal = command.exitValue();
            if (exitVal != 0) {
                System.out.println("Error al ejecutar jar | " + getExecutionLog(exitVal, error, op));
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error al ejecutar jar | " + e);
            return false;
        }
        return true;
    }
}
