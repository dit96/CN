---server

import java.net.*;
import java.io.*;

public class server {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        BufferedReader inputReader = null;
        PrintWriter outputWriter = null;

        try {
            // Create a ServerSocket listening on port 6789
            serverSocket = new ServerSocket(6789);
            System.out.println("Server is waiting for a connection...");

            // Accept an incoming connection
            clientSocket = serverSocket.accept();
            System.out.println("Client connected.");

            // Set up input and output streams for communication
            inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outputWriter = new PrintWriter(clientSocket.getOutputStream(), true);

            // Receive the message from the client
            String message = inputReader.readLine();
            System.out.println("Received from client: " + message);

            // Send a response to the client
            outputWriter.println("Hello, Client! This is the Server.");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputReader != null) inputReader.close();
                if (outputWriter != null) outputWriter.close();
                if (clientSocket != null) clientSocket.close();
                if (serverSocket != null) serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


---client

import java.net.*;
import java.io.*;

public class client {
    public static void main(String[] args) {
        Socket socket = null;
        BufferedReader userInput = null;
        PrintWriter outputWriter = null;
        BufferedReader inputReader = null;

        try {
            // Create a socket connection to the server at localhost, port 6789
            socket = new Socket("localhost", 6789);
            System.out.println("Connected to the server.");

            // Set up input and output streams for communication
            userInput = new BufferedReader(new InputStreamReader(System.in));
            outputWriter = new PrintWriter(socket.getOutputStream(), true);
            inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Send a message to the server
            System.out.print("Enter your message: ");
            String message = userInput.readLine();
            outputWriter.println(message);

            // Receive and display the server's response
            String response = inputReader.readLine();
            System.out.println("Received from server: " + response);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (userInput != null) userInput.close();
                if (outputWriter != null) outputWriter.close();
                if (inputReader != null) inputReader.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

