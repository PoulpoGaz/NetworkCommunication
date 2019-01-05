package fr.PoulpoGaz.NetworkCommunication.network;

import fr.PoulpoGaz.NetworkCommunication.Main;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class Network implements Runnable {

    protected String ip, name;
    protected int port;
    protected Main m;

    protected PrintWriter writer;
    protected BufferedInputStream reader;
    protected boolean running = true;

    protected boolean error = false;

    public Network(String host, int p, Main m, String username) {
        ip = host;
        port = p;
        this.m = m;
        name = username;
    }

    public String read() throws IOException {
        String response;
        int stream;
        byte[] b = new byte[4096];
        stream = reader.read(b);
        if(stream < 0) {
            stream = 0;
        }
        response = new String(b, 0, stream);
        return response;
    }

    public void send(String message) throws IOException {
        writer.write("[" + name +  "] >> " + message);
        writer.flush();
    }

    public void sendAndClose() throws IOException {
        writer.write("[" + name +  "] >> Est déconnecté(e).");
        writer.flush();
        running = false;
    }

    public String getName() {
        return name;
    }

    public boolean isError() {
        return error;
    }
}
