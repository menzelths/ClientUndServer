/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverundclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author menze
 */
public class ClientWhile {

    public static void main(String[] args) {

        String eingabeKonsole = "";
        String serverAntwort = "";
        boolean weiter = true, weiterEingabe = true;

        try {

            BufferedReader konsole = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Bitte Host eingeben: ");
            String host = konsole.readLine();
            System.out.print("Bitte Port eingeben: ");
            int port = Integer.parseInt(konsole.readLine());

            Socket socket = new Socket(java.net.InetAddress.getByName(host), port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream out = socket.getOutputStream();

            while (weiter) {
                System.out.println("\n\nBitte zu sendende Nachricht eingeben (\":-)\" zum Abschließen der Nachricht, \"Ende\" zum Beenden):");
                String text = "";
                weiterEingabe=true;
                while (weiterEingabe) {
                    eingabeKonsole = konsole.readLine();
                    if (eingabeKonsole.equals("Ende")) {
                        weiterEingabe=false;
                        weiter = false;
                    } else if (eingabeKonsole.equals(":-)")) {
                        out.write(text.getBytes());
                        out.write('\n');

                        System.out.println("\nHabe den folgenden Text gesendet:\n"+text);
                        
                        while ((serverAntwort = in.readLine()) != null) {
                            System.out.println(serverAntwort);
                        }
                        weiterEingabe = false;
                    } else {
                        text += eingabeKonsole + "\n";
                    }
                }

            }

            in.close();
            out.close();
            socket.close();
            System.out.println("Client erfolgreich beendet ...");

        } catch (Exception e) {
            System.out.println("Fehler aufgetreten: " + e.toString());
            System.exit(1);
        }

    }
}
