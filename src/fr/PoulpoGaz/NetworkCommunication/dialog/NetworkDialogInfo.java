package fr.PoulpoGaz.NetworkCommunication.dialog;

public class NetworkDialogInfo {

    private boolean clientOrServer;
    private String ip;
    private int port;
    private String pseudo;

    public NetworkDialogInfo(int state, String ip, int port, String pseudo) {
        clientOrServer = state == 0;
        this.ip = ip;
        this.port = port;
        this.pseudo = pseudo;
    }

    public String getState() {
        if(clientOrServer) {
            return "Hôte";
        } else {
            return "Client";
        }
    }

    public boolean isClientOrServer() {
        return clientOrServer;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getPseudo() {
        return pseudo;
    }
}
