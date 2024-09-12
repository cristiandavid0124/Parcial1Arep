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
                                        // Handling methods with different numbers of parameters
                                        Method methodToCall;
                                        Object result;

                                        // Determine the method to call based on the number of parameters
                                        switch (methodName) {
                                            case "max":
                                                methodToCall = Math.class.getMethod("max", double.class, double.class);
                                                result = methodToCall.invoke(null, Double.parseDouble(parameters[0]), Double.parseDouble(parameters[1]));
                                                break;
                                            case "min":
                                                methodToCall = Math.class.getMethod("min", double.class, double.class);
                                                result = methodToCall.invoke(null, Double.parseDouble(parameters[0]), Double.parseDouble(parameters[1]));
                                                break;
                                            case "pow":
                                                methodToCall = Math.class.getMethod("pow", double.class, double.class);
                                                result = methodToCall.invoke(null, Double.parseDouble(parameters[0]), Double.parseDouble(parameters[1]));
                                                break;
                                            case "sqrt":
                                                methodToCall = Math.class.getMethod("sqrt", double.class);
                                                result = methodToCall.invoke(null, Double.parseDouble(parameters[0]));
                                                break;
                                            default:
                                                throw new NoSuchMethodException("Method " + methodName + " not found.");
                                        }

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

    public static double[] bubbleSort(double[] array) {
        int n = array.length;
        double temp;
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < (n - i); j++) {
                if (array[j - 1] > array[j]) {
                    temp = array[j - 1];
                    array[j - 1] = array[j];
                    array[j] = temp;
                }
            }
        }
        return array;
    }
}
