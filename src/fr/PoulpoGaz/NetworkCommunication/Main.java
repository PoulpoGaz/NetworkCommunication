package fr.PoulpoGaz.NetworkCommunication;

import fr.PoulpoGaz.NetworkCommunication.dialog.NetworkDialog;
import fr.PoulpoGaz.NetworkCommunication.dialog.NetworkDialogInfo;
import fr.PoulpoGaz.NetworkCommunication.network.client.Client;
import fr.PoulpoGaz.NetworkCommunication.network.server.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class Main extends JFrame {

    public static void main(String[] args) {
        new Main();
    }

    private Server server;
    private Client client;
    private boolean isServer;

    private JTextField input = new JTextField();
    private JTextArea output = new JTextArea();

    private boolean connection = false;

    public Main() {
        //Init look and fell
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }

        NetworkDialog net = new NetworkDialog(null, "NetworkDialog", true);
        NetworkDialogInfo netInfo = net.getInfo();

        while(!initNetwork(netInfo)) {
            netInfo = net.getInfo();
        }

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(getConnection()) {
                    try {
                        if(isServer()) {
                            server.sendAndClose();
                        } else {
                            client.sendAndClose();
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

                System.exit(0);
            }
        });
        setResizable(false);
        setTitle("Network Communication: " + netInfo.getPseudo() + " ("+ netInfo.getState() + ")");
        initComponent();
        setSize(new Dimension(500,500));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponent() {
        JPanel top = new JPanel();

        input.setPreferredSize(new Dimension(400,25));

        JButton send = new JButton("Envoyer");
        send.setPreferredSize(new Dimension(100, 25));
        send.addActionListener(new SendListenner());

        top.setLayout(new BoxLayout(top, BoxLayout.LINE_AXIS));
        top.add(input);
        top.add(send);

        output.setLineWrap(true);
        output.setEditable(false);

        JScrollPane outputScroll = new JScrollPane(output);
        outputScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(top, BorderLayout.NORTH);
        add(outputScroll, BorderLayout.CENTER);
    }

    private boolean initNetwork(NetworkDialogInfo netInfo) {
        if(netInfo.isClientOrServer()) {
            server = new Server(netInfo.getIp(), netInfo.getPort(), this, netInfo.getPseudo());

            if(!server.isError()) {
                Thread t = new Thread(server);
                t.start();
                isServer = true;
                return true;
            } else {
                return false;
            }

        } else {
            client = new Client(netInfo.getIp(), netInfo.getPort(), this, netInfo.getPseudo());

            if(!client.isError()) {
                Thread t = new Thread(client);
                t.start();
                isServer = false;
                return true;
            } else {
                return false;
            }
        }
    }

    public void update(String response) {
        output.setText(output.getText() + "\n" + response);
    }

    public boolean isServer() {
        return isServer;
    }

    public void setConnection(boolean connect) {
        connection = connect;
    }

    public boolean getConnection() {
        return connection;
    }

    private class SendListenner implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(getConnection()) {
                try {
                    String send = input.getText();

                    if(send.length() > 65536) {
                        JOptionPane.showMessageDialog(null, "Message trop grand. La limite est 65536 caractères.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }

                    if(isServer()) {
                        server.send(send);
                        update("[" + server.getName() +  "] >> " + input.getText());
                    } else {
                        client.send(send);
                        update("[" + client.getName() +  "] >> " + input.getText());
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Impossible d'envoyer un message. Personne n'est connecté", "Impossible d'envoyer un message", JOptionPane.INFORMATION_MESSAGE);
            }
            input.setText("");
        }
    }
}