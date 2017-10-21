/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverundclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author menze
 */
public class ServerHttp {

    public static void main(String[] args) {
        int counter = 0;
        try {
            System.out.println("IP-Adresse: " + Inet4Address.getLocalHost().getHostAddress());
        } catch (UnknownHostException ex) {

        }
        try {
            System.out.println("Warte auf Port 8080 auf Clients");
            ServerSocket simpleserver = new ServerSocket(8080);

            while (true) {
                Socket simplesocket = simpleserver.accept();
                (new serverHttp_thread(++counter, simplesocket)).start();
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
}

class serverHttp_thread extends Thread {

    private byte name;
    private String text, eingabe;
    private Socket simplesocket;

    public serverHttp_thread(int name, Socket simplesocket) {
        this.name = (byte) name;
        this.simplesocket = simplesocket;
    }

    public void run() {
        try {

            System.out.println("\nHabe Verbindung zu Client " + name + " hergestellt!\n");
            OutputStream out = simplesocket.getOutputStream();
            BufferedReader in_socket = new BufferedReader(new InputStreamReader(simplesocket.getInputStream()));

            boolean weiter = true;
            List<String> liste = new ArrayList<>();
            while (weiter == true && (eingabe = in_socket.readLine()) != null) {
                liste.add(eingabe);
                if (eingabe.trim().length() == 0) {
                    weiter = false;
                }
                System.out.println("Client " + name + ": '" + eingabe + "'");

            }

            System.out.println("\nSchicke eine Antwort ...\n");

            // nur die GET-Zeile heraussuchen, falls vorhanden
            String anfrage = liste.stream()
                    .filter(e -> e.startsWith("GET ")) // alle Zeilen mit "GET " herausfiltern
                    .findFirst() // nur die erste nehmen
                    .map(p -> p) // zurückgeben
                    .orElseGet(() -> ""); // falls nicht vorhanden, den leeren String zurückgeben

            String[] teile = anfrage.split(" ");

            if (teile.length > 2) {
                String content = "<html><body><h1>Es funktioniert!</h1>Du bist auf der Seite " + teile[1] + ".<br>Hier ein Link: <a href='http://www.esg.lb.schule-bw.de/index.php?id=251'>Zur Schulhomepage</a></html>";
                String antwortText = "HTTP/1.1 200 OK\nContent-Length: " + content.length() + "\nConnection: close\nContent-Type: text/html\n\n" + content + "\n";

                out.write(antwortText.getBytes());
            }

            out.close();
            in_socket.close();
            simplesocket.close();
        } catch (IOException e) {
            System.out.println("Fehler aufgetreten..." + e.toString());
            System.exit(1);
        }

        System.out.println("\nVerbindung mit Client " + name + " beendet!");
    }
}
