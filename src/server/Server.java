package server;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        int port = 5000;

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Relay Server Started on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected!");

                ClientHandler clientHandler = new ClientHandler(socket);
                clientHandler.start();
            }

        } catch (Exception e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}