package fr.PoulpoGaz.NetworkCommunication.network.server;

import fr.PoulpoGaz.NetworkCommunication.Main;
import fr.PoulpoGaz.NetworkCommunication.network.Network;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Network {

    private ServerSocket serverSocket;
    private Socket client;

    public Server(String host, int p, Main m, String username) {
        super(host, p, m, username);

        try {
            serverSocket = new ServerSocket(port, 100, InetAddress.getByName(host));

            System.out.println("Création d'un serveur sur le port: " + port + " avec l'ip: " + ip);
        } catch (BindException e) {
            JOptionPane.showMessageDialog(null, "Le serveur avec le port: " + port + " et avec l'ip: " + ip + "\nExiste déjà");
            error = true;
        } catch (IOException e) {
            error = true;
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            client = serverSocket.accept();
            m.setConnection(true);

            writer = new PrintWriter(client.getOutputStream());
            reader = new BufferedInputStream(client.getInputStream());
            send("Connecté!");

            while (running) {
                writer = new PrintWriter(client.getOutputStream());
                reader = new BufferedInputStream(client.getInputStream());

                String str = read();
                m.update(str);
                if(str.contains("Est déconnecté(e).")) {
                    running = false;
                }
            }

            writer.close();
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        if(serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                serverSocket = null;
            }
        }
    }
}
