package org.edu.eci.arep;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

public class HttpCalculatorServer {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(36001)) {
            System.out.println("Calculator server running on port 36001");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                    String inputLine;
                    String uriString = "";

                    while ((inputLine = in.readLine()) != null) {
                        if (inputLine.startsWith("GET")) {
                            uriString = inputLine.split(" ")[1];
                            break;
                        }
                    }

                    URI requestURI = new URI(uriString);
                    String path = requestURI.getPath();
                    String query = requestURI.getQuery();
                    String outputLine;

                    if ("/compreflex".equals(path) && query != null) {
                        String[] queryParts = query.split("=");
                        if (queryParts.length == 2) {
                            String command = queryParts[1];
                            String[] commandParts = command.split("\\(");
                            if (commandParts.length == 2) {
                                String methodName = commandParts[0].trim();
                                String[] parameters = commandParts[1].replace(")", "").split(",");
                                try {
                                    if ("bbl".equals(methodName)) {
                                        double[] params = Arrays.stream(parameters).mapToDouble(Double::parseDouble).toArray();
                                        double[] sorted = bubbleSort(params);
                                        outputLine = "{\"Resultado\": \"" + Arrays.toString(sorted) + "\"}";
                                    } else {
                                        Method methodToCall = Math.class.getMethod(methodName, double[].class);
                                        double[] params = Arrays.stream(parameters).mapToDouble(Double::parseDouble).toArray();
                                        Object result = methodToCall.invoke(null, new Object[]{params});
                                        outputLine = "{\"Resultado\": \"" + result + "\"}";
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    outputLine = "{\"Error\": \"Invalid command or parameters\"}";
                                }
                            } else {
                                outputLine = "{\"Error\": \"Invalid command format\"}";
                            }
                        } else {
                            outputLine = "{\"Error\": \"Invalid query format\"}";
                        }
                    } else {
                        outputLine = "{\"Error\": \"Invalid endpoint\"}";
                    }

                    out.println("HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n" + outputLine);
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static double[] bubbleSort(double[] array) {
        int n = array.length;
        boolean swapped;
        do {
            swapped = false;
            for (int i = 1; i < n; i++) {
                if (array[i - 1] > array[i]) {
                    double temp = array[i - 1];
                    array[i - 1] = array[i];
                    array[i] = temp;
                    swapped = true;
                }
            }
            n--;
        } while (swapped);
        return array;
    }
}
