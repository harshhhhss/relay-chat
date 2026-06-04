package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final Object CONSOLE_LOCK = new Object();

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

            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            output.println(username);

            Thread messageListener = new Thread(() -> {
                try {
                    String message;
                    while ((message = input.readLine()) != null) {
                        displayIncomingMessage(message);
                    }
                } catch (IOException e) {
                    synchronized (CONSOLE_LOCK) {
                        System.out.println("\nDisconnected from server.");
                    }
                }
            });
            messageListener.setDaemon(true);
            messageListener.start();

            while (true) {
                displayPrompt();
                String message = scanner.nextLine();

                output.println(message);

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

    private static void displayIncomingMessage(String message) {
        synchronized (CONSOLE_LOCK) {
            System.out.println();
            System.out.println(message);
            System.out.print("You: ");
            System.out.flush();
        }
    }

    private static void displayPrompt() {
        synchronized (CONSOLE_LOCK) {
            System.out.print("You: ");
            System.out.flush();
        }
    }
}
