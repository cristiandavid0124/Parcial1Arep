package org.edu.eci.arep;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class HttpServer {
   
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String GET_URL = "http://localhost:36001/compreflex?";

    public static void main(String[] args) throws IOException, URISyntaxException {
        try (ServerSocket serverSocket = new ServerSocket(35000)) {
            System.out.println("Server running on port 35000");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                    String inputLine;
                    String uriString = "";
                    boolean firstLine = true;

                    while ((inputLine = in.readLine()) != null) {
                        if (firstLine) {
                            uriString = inputLine.split(" ")[1];
                            firstLine = false;
                        }
                        if (!in.ready()) {
                            break;
                        }
                    }

                    String outputLine;
                    URI requestURI = new URI(uriString);
                    String path = requestURI.getPath();

                    if ("/clieente".equals(path)) {
                        outputLine = HttpBuilder.getHttpClient();
                    } else if ("/consulta".equals(path)) {
                        outputLine = httpConnectionAPI(requestURI.getQuery());
                    } else {
                        outputLine = HttpBuilder.httpNotFound();
                    }

                    out.println(outputLine);
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public static String httpConnectionAPI(String query) throws IOException {
        URL url = new URL(GET_URL + query);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                return HttpBuilder.httpJSONResponse(response.toString());
            }
        } else {
            return HttpBuilder.httpNotFound();
        }
    }
}
