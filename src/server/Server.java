package server;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Relay Server Started...");
            System.out.println("Waiting for client...");

            Socket socket = serverSocket.accept();
            System.out.println("Client Connected!");

            BufferedReader input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            PrintWriter output = new PrintWriter(
                    socket.getOutputStream(), true);

            Scanner scanner = new Scanner(System.in);

            while (true) {
                String clientMessage = input.readLine();

                if (clientMessage.equalsIgnoreCase("exit")) {
                    System.out.println("Client left the chat.");
                    break;
                }

                System.out.println("Client: " + clientMessage);

                System.out.print("Server: ");
                String serverMessage = scanner.nextLine();

                output.println(serverMessage);

                if (serverMessage.equalsIgnoreCase("exit")) {
                    break;
                }
            }

            scanner.close();
            input.close();
            output.close();
            socket.close();
            serverSocket.close();

        } catch (Exception e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}