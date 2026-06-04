package server;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientHandler extends Thread {
    private static final List<ClientHandler> clientHandlers = new CopyOnWriteArrayList<>();

    private Socket socket;
    private PrintWriter output;
    private String username;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            output = new PrintWriter(
                    socket.getOutputStream(), true);

            output.println("Welcome to Relay Chat!");

            username = input.readLine();
            if (username == null) {
                return;
            }

            clientHandlers.add(this);

            String message;

            while ((message = input.readLine()) != null) {
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }

                String formattedMessage = username + ": " + message;
                System.out.println(formattedMessage);
                broadcast(formattedMessage);
            }

        } catch (Exception e) {
            System.out.println("Client disconnected.");
        } finally {
            clientHandlers.remove(this);
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Error closing client socket.");
            }
        }
    }

    private void broadcast(String message) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler != this) {
                clientHandler.output.println(message);
            }
        }
    }
}
