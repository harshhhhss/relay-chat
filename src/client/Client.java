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

            String welcome = input.readLine();
            System.out.println("Server: " + welcome);

            while (true) {
                System.out.print("You: ");
                String message = scanner.nextLine();

                output.println(message);

                String reply = input.readLine();
                System.out.println("Server: " + reply);

                if (message.equalsIgnoreCase("exit")) {
                    break;
                }
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