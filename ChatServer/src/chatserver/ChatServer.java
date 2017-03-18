/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

/**
 *
 * @author jota
 */
public class ChatServer {

    private static SSLSocket socket;
    private static InputStream streamIn;
    static SSLServerSocketFactory serverSocketFactory;
    static SSLServerSocket server;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.setProperty("javax.net.ssl.keyStore", "serverKey.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "servpass");
        System.setProperty("javax.net.ssl.trustStore", "serverTrustedCerts.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "servpass");
        serverSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        try {
            System.out.println("Binding to port " + 5555 + ", please wait  ...");
            server = (SSLServerSocket) serverSocketFactory.createServerSocket();
            server.bind(new InetSocketAddress("localhost", 5555));
            System.out.println("Server started: " + server);
            System.out.println("Waiting for a client ...");
            socket = (SSLSocket) server.accept();
            System.out.println("Client accepted: " + socket);
            open();
            String mensaje = "";
            while (true) {
                char c = (char) streamIn.read();
                while (c != -1) {
                    mensaje += c;
                    if (streamIn.available() != 0) {
                        c = (char) streamIn.read();
                    } else {
                        break;
                    }
                }
                System.out.println("Cliente: " + mensaje);
                if(mensaje.equals(".bye")){
                    break;
                }
                mensaje = "";
            }
            close();

        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

    public static void open() throws IOException {
        streamIn = socket.getInputStream();
    }

    public static void close() throws IOException {
        if (socket != null) {
            socket.close();
        }
        if (streamIn != null) {
            streamIn.close();
        }

    }

}
