package fr.PoulpoGaz.NetworkCommunication.dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkDialog extends JDialog {

    private NetworkDialogInfo info;
    private JTextField ip = null;
    private JTextField port = null;
    private JTextField pseudo = null;

    public NetworkDialog(JFrame parent, String title, boolean modal) {
        super(parent, title, modal);
        setResizable(false);
        initComponent();
        pack();
        setLocationRelativeTo(null);
    }

    public NetworkDialogInfo getInfo() {
        setVisible(true);
        return info;
    }

    public void initComponent() {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

        //HOTE OU CLIENT
        JPanel radioPanel = new JPanel();
        radioPanel.setBorder(BorderFactory.createTitledBorder("Hôte ou client"));
        radioPanel.setBackground(Color.WHITE);

        final JRadioButton hote = new JRadioButton("Hôte");
        hote.setBackground(Color.WHITE);
        final JRadioButton client = new JRadioButton("Client");
        client.setBackground(Color.WHITE);

        ButtonGroup bg = new ButtonGroup();
        bg.add(hote);
        bg.add(client);

        radioPanel.add(hote);
        radioPanel.add(client);

        //Paramètres
        JPanel settings = new JPanel();
        settings.setBorder(BorderFactory.createTitledBorder("Paramètres: "));
        settings.setBackground(Color.WHITE);

        //IP
        JPanel ipPanel = new JPanel();
        ipPanel.setBackground(Color.WHITE);

        JButton localIp = new JButton("Ip local");
        localIp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ip.setText(InetAddress.getLocalHost().getHostAddress());
                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                }
            }
        });

        JLabel ipLabel = new JLabel("    Ip: ");
        ipLabel.setBackground(Color.WHITE);

        ip = new JTextField();
        ip.setPreferredSize(new Dimension(100, 25));

        ipPanel.add(localIp);
        ipPanel.add(ipLabel);
        ipPanel.add(ip);

        //Port
        JPanel portPanel = new JPanel();
        portPanel.setBackground(Color.WHITE);

        JLabel portLabel = new JLabel("Port: ");
        portLabel.setBackground(Color.WHITE);

        port = new JTextField();
        port.setPreferredSize(new Dimension(100, 25));

        portPanel.add(portLabel);
        portPanel.add(port);

        settings.add(ipPanel);
        settings.add(portPanel);

        //Pseudo
        JPanel pseudoPanel = new JPanel();
        pseudoPanel.setBorder(BorderFactory.createTitledBorder("Pseudonyme"));
        pseudoPanel.setBackground(Color.WHITE);

        final JLabel pseudoLabel = new JLabel("Pseudo: ");
        pseudoLabel.setBackground(Color.WHITE);

        pseudo = new JTextField();
        pseudo.setPreferredSize(new Dimension(200, 25));

        pseudoPanel.add(pseudoLabel);
        pseudoPanel.add(pseudo);

        //End dialog
        JPanel confirm = new JPanel();
        confirm.setBackground(Color.WHITE);

        JButton ok = new JButton("Valider");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean state = getState() != 2;
                boolean ipB = checkIp(ip.getText());
                boolean portB = checkPort(port.getText());
                boolean pseudoB = !pseudo.getText().equals("");

                if(state && ipB && portB && pseudoB) {
                    info = new NetworkDialogInfo(getState(), ip.getText(), Integer.valueOf(port.getText()), pseudo.getText());
                    dispose();
                } else {
                    String str = "Le(s) champ(s) suivant(s) est/sont manquant ou incorect:";
                    if(!state) {
                        str += "\n -Le champ hôte ou client";
                    }
                    if(!ipB) {
                        str += "\n -Le champ ip";
                    }
                    if(!portB) {
                        str += "\n -Le champ port";
                    }
                    if(!pseudoB) {
                        str += "\n -Le champ pseudonyme";
                    }
                    JOptionPane.showMessageDialog(null, str, "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }

            public int getState(){
                return (hote.isSelected()) ? 0 :
                        (client.isSelected()) ? 1 : 2;
            }

            public boolean checkIp(String ip) {
                String[] tabIp = ip.split("\\.");
                try {
                    if (tabIp.length!=4) {
                        return false;
                    }
                    int digitIp;
                    for(String TabIp : tabIp) {
                        digitIp = Integer.parseInt(TabIp);
                        if (digitIp < 0 || digitIp > 255) {
                            return false;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                return true;
            }

            public boolean checkPort(String p) {
                int port;
                try {
                    port = Integer.valueOf(p);
                } catch (NumberFormatException e) {
                    return false;
                }
                if(port > 1023 && port < 65535) {
                    return true;
                }
                return false;
            }
        });

        JButton anul = new JButton("Annuler");
        anul.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                System.exit(0);
            }
        });

        confirm.add(ok);
        confirm.add(anul);

        add(radioPanel);
        add(settings);
        add(pseudoPanel);
        add(confirm);
    }
}