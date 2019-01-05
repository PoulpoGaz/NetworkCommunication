package fr.PoulpoGaz.NetworkCommunication.network.client;

import fr.PoulpoGaz.NetworkCommunication.Main;
import fr.PoulpoGaz.NetworkCommunication.network.Network;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends Network {

    private Socket client;

    public Client(String host, int p, Main m, String username) {
        super(host, p, m, username);

        try {
            client = new Socket(host, p);
            System.out.println("Connexion avec le serveur\nIP: " + ip + "\nPORT: " + port);
            m.setConnection(true);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Le serveur est indisponible\nIP: " + ip + "\nPORT: " + port, "Erreur", JOptionPane.ERROR_MESSAGE);
            error = true;
        }
    }

    @Override
    public void run() {
        try {
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
        m.setConnection(false);

        if(client != null) {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                client = null;
            }
        }
    }
}