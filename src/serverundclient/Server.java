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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author menze
 */
public class Server {

    public static void main(String[] args) {
        int counter = 0;
        try {
            System.out.println("IP-Adresse: " + Inet4Address.getLocalHost().getHostAddress());
        } catch (UnknownHostException ex) {

        }
        try {
            System.out.println("Warte auf Port 6000 auf Clients");
            ServerSocket simpleserver = new ServerSocket(6000);

            while (true) {
                Socket simplesocket = simpleserver.accept();
                (new server_thread(++counter, simplesocket)).start();
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
}

class server_thread extends Thread {

    private byte name;
    private String text, eingabe;
    private Socket simplesocket;

    public server_thread(int name, Socket simplesocket) {
        this.name = (byte) name;
        this.simplesocket = simplesocket;
    }

    public void run() {
        try {

            System.out.println("\nHabe Verbindung zu Client " + name + " hergestellt!");
            OutputStream out = simplesocket.getOutputStream();
            BufferedReader in_socket = new BufferedReader(new InputStreamReader(simplesocket.getInputStream()));

            text = "Hallo Client Nr. " + name + ": Gib ein Wort ein\n";
            out.write(text.getBytes());
            eingabe = in_socket.readLine();

            text = "Hallo Client Nr. " + name + ": Dein Wort war " + eingabe + ". Und tschüss!\n";
            out.write(text.getBytes());

            System.out.println("\nDas Wort von Client " + name + " war " + eingabe);

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
