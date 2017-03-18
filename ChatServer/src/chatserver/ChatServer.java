/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.io.IOException;
import java.io.InputStream;
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
        /*
        Añadimos estas propiedades en la ejecución del servidor para que sepa
        donde está su almacén de claves y los certificados en los que confía
        */
        System.setProperty("javax.net.ssl.keyStore", "serverKey.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "servpass");
        System.setProperty("javax.net.ssl.trustStore", "serverTrustedCerts.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "servpass");
        //Instanciamos el SSLServerSocketFactory que nos permitirá posteriormente crear
        //el socket SSL
        serverSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        try {
            System.out.println("Binding to port " + 5555 + ", please wait  ...");
            //Creamos el socket SSL para el servidor
            server = (SSLServerSocket) serverSocketFactory.createServerSocket();
            //Le decimos el puerto y dirección en la que escuchará el socket
            server.bind(new InetSocketAddress("localhost", 5555));
            System.out.println("Server started: " + server);
            System.out.println("Waiting for a client ...");
            //El socket se pondrá a la escucha de peticiones entrantes por parte
            //de los clientes
            socket = (SSLSocket) server.accept();
            System.out.println("Client accepted: " + socket);
            open(); //Abrimos el flujo de entrada al servidor desde el cliente
            String mensaje = "";
            while (true) {
                char c = (char) streamIn.read(); //Leemos del flujo el primer byte que encuentre y los casteamos a un caracter
                while (c != -1) {
                    mensaje += c; //Añadimos el caracter leído al mensaje final
                    if (streamIn.available() != 0) { //Si hay algo más que leer lo lee y sigue el proceso anterior,
                        //en caso contrario fuerza la salida del bucle de lectura
                        c = (char) streamIn.read();
                    } else {
                        break;
                    }
                }
                System.out.println("Cliente: " + mensaje); //Muestra el mensaje recibido
                if(mensaje.equals(".bye")){ //Si el mensaje es .bye cierra el servidor
                    break;
                }
                mensaje = ""; //reinicio la variable del mensaje para seguir leyendo el próximo mensaje
            }
            close(); //Cierro el servidor

        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }
    
    //Método para abrir el flujo de entrada al servidor desde el cliente
    public static void open() throws IOException {
        streamIn = socket.getInputStream();
    }
    
    //Método que cierra el flujo de entrada y el socket
    public static void close() throws IOException {
        if (socket != null) {
            socket.close();
        }
        if (streamIn != null) {
            streamIn.close();
        }

    }

}
