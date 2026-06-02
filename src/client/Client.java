package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 5000);
            System.out.println("Connected to Relay Server!");

            BufferedReader input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            PrintWriter output = new PrintWriter(
                    socket.getOutputStream(), true);

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.print("You: ");
                String clientMessage = scanner.nextLine();

                output.println(clientMessage);

                if (clientMessage.equalsIgnoreCase("exit")) {
                    break;
                }

                String serverReply = input.readLine();

                if (serverReply.equalsIgnoreCase("exit")) {
                    System.out.println("Server closed the chat.");
                    break;
                }

                System.out.println("Server: " + serverReply);
            }

            scanner.close();
            input.close();
            output.close();
            socket.close();

        } catch (Exception e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }
}