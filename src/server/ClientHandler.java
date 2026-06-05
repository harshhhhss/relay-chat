package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private String username;

    public ClientHandler(Socket socket) {
        this.socket = socket;

        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Error setting up client: " + e.getMessage());
            closeSocket();
        }
    }

    @Override
    public void run() {
        try {
            username = input.readLine();

            if (username == null || username.trim().isEmpty()) {
                username = "Unknown";
            }

            broadcast(username + " joined the chat", this);

            String message;
            while ((message = input.readLine()) != null) {
                if (!message.trim().isEmpty()) {
                    broadcast(username + ": " + message, this);
                }
            }

        } catch (IOException e) {
            // Client disconnected unexpectedly
        } finally {
            removeClient();
        }
    }

    private void broadcast(String message, ClientHandler sender) {
        System.out.println(message);

        synchronized (Server.clients) {
            for (ClientHandler client : Server.clients) {
                if (client != sender) {
                    client.output.println(message);
                }
            }
        }
    }

    private void removeClient() {
        synchronized (Server.clients) {
            Server.clients.remove(this);
        }

        if (username != null && !username.trim().isEmpty()) {
            broadcast(username + " left the chat", this);
        }

        closeSocket();
    }

    private void closeSocket() {
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}