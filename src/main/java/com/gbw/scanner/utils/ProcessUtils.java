package com.gbw.scanner.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ProcessUtils {

    public static String executeCommand(String[] command) {

        StringBuilder output = new StringBuilder();

        try {
            Process p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        } catch (Exception e) {
            return e.getMessage();
        }

        return output.toString();
    }
}
