package server;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            PrintWriter output = new PrintWriter(
                    socket.getOutputStream(), true);

            output.println("Welcome to Relay Chat!");

            String message;

            while ((message = input.readLine()) != null) {
                if (message.equalsIgnoreCase("exit")) {
                    output.println("Goodbye!");
                    break;
                }

                System.out.println("Client: " + message);
                output.println("Server received: " + message);
            }

            input.close();
            output.close();
            socket.close();

        } catch (Exception e) {
            System.out.println("Client disconnected.");
        }
    }
}