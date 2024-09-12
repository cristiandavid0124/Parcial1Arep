package org.edu.eci.arep;

public class HttpBuilder {

    public static String getHttpClient(){
        String outputLine = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n" +
                "<!DOCTYPE html>\n" +
                "<!DOCTYPE html>\n" +
                "<html>\n" +
                "    <head>\n" +
                "        <title>Reflective Calculadora</title>\n" +
                "        <meta charset=\"UTF-8\">\n" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <h1>Reflective Calculadora</h1>\n" +
                "        <form action=\"/consulta\">\n" +
                "            <label for=\"comando\">Comando:</label><br>\n" +
                "            <input type=\"text\" id=\"comando\" name=\"comando\"><br><br>\n" +
                "            <input type=\"button\" value=\"Submit\" onclick=\"loadGetMsg()\">\n" +
                "        </form> \n" +
                "        <div id=\"getrespmsg\"></div>\n" +
                "\n" +
                "        <script>\n" +
                "            function loadGetMsg() {\n" +
                "                let nameVar = document.getElementById(\"comando\").value;\n" +
                "                const xhttp = new XMLHttpRequest();\n" +
                "                xhttp.onload = function() {\n" +
                "                    document.getElementById(\"getrespmsg\").innerHTML =\n" +
                "                    this.responseText;\n" +
                "                }\n" +
                "                xhttp.open(\"GET\", \"/consulta?comando=\"+nameVar);\n" +
                "                xhttp.send();\n" +
                "            }\n" +
                "        </script>\n" +
                "    </body>\n" +
                "</html>";
        return  outputLine;
    }

    public static String httpNotFound(){
        String outputLine = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                +
                "<!DOCTYPE html>\n" +
                "<html>\n" +
                "    <head>\n" +
                "        <title>Form Example</title>\n" +
                "        <meta charset=\"UTF-8\">\n" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <h1>Resorce not found!</h1>\n" +
                "    </body>\n" +
                "</html>";
        return  outputLine;
    }

    public static String httpJSONResponse(String data){
        String outputLine = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: application/json\"\r\n"
                + "\r\n"
                + data;
        return  outputLine;
    }
}