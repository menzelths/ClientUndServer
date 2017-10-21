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
public class Client {
    
    public static void main(String[] args) {
 
        String eingabe="";
        
        try{
        	Socket socket=new Socket(java.net.InetAddress.getByName("localhost"),6000);
        	BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        	BufferedReader eingabeKonsole=new BufferedReader(new InputStreamReader(System.in));
        	OutputStream out=socket.getOutputStream();
        
        	eingabe=in.readLine();
        	System.out.println(eingabe);
        
        	eingabe=eingabeKonsole.readLine();
        	out.write(eingabe.getBytes());
        	out.write('\n');
        
        	eingabe=in.readLine();
        	System.out.println(eingabe);
     
        
        	System.out.println("");
        	
        	in.close();
        	out.close();
        	socket.close();
                System.out.println("Client erfolgreich beendet ...");
        } catch (Exception e){
        	System.out.println("Fehler aufgetreten: "+e.toString());
        	System.exit(1);
        } 
        
    }    
}
