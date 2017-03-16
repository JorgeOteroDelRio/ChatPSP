/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

/**
 *
 * @author jota
 */
public class ChatServer {

    private static Socket socket;
    private static DataInputStream streamIn;
    private static DataOutputStream streamOut;
    static SSLServerSocketFactory serverSocketFactory;
    static SSLServerSocket server;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.setProperty("javax.net.ssl.keystore", "mySrvKeystore");
        System.setProperty("javax.net.ssl.trustStore", "mySrvKeystore");
        System.setProperty("javax.net.ssl.keyStorePassword", "003100729011");
        serverSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        try {
            System.out.println("Binding to port " + 5555 + ", please wait  ...");
            server = (SSLServerSocket) serverSocketFactory.createServerSocket();
            server.bind(new InetSocketAddress("localhost",5555));
            System.out.println("Server started: " + server);
            System.out.println("Waiting for a client ...");
            socket = server.accept();
            System.out.println("Client accepted: " + socket);
            open();
            boolean done = false;
            while (!done) {
                String line = streamIn.readUTF();
                System.out.println("Cliente: " + line);
                streamOut.writeUTF(line);
                done = line.equals(".bye");

            }
            close();
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

    public static void open() throws IOException {
        streamIn = new DataInputStream(socket.getInputStream());
        streamOut = new DataOutputStream(socket.getOutputStream());
    }

    public static void close() throws IOException {
        if (socket != null) {
            socket.close();
        }
        if (streamIn != null) {
            streamIn.close();
        }
        if (streamOut != null) {
            streamOut.close();
        }
    }

    public static void crearClave(){
        try {
            System.out.println("Obteniendo generador de claves con cifrado DES");
            KeyGenerator keygen = KeyGenerator.getInstance("DES");
            System.out.println("Generando clave");
            SecretKey key = keygen.generateKey();
            System.out.println("Obteniendo factoria de claecs con cifrado DES");
            SecretKeyFactory keyfac = SecretKeyFactory.getInstance("DES");
            System.out.println("Generando keyspec");
            KeySpec keypsec = keyfac.getKeySpec(key, ChatServer.class);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
