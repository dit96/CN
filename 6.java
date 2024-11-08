---server

import java.net.*;

public class server {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        try {
            // Create a DatagramSocket to receive data
            socket = new DatagramSocket(9876);
            byte[] receiveData = new byte[1024];
            
            System.out.println("Server is ready to receive messages...");

            while (true) {
                // Receive incoming data from the client
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);

                // Extract the message from the packet
                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received message: " + message);

                // Send an acknowledgment back to the client
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                String response = "Message received: " + message;
                DatagramPacket sendPacket = new DatagramPacket(response.getBytes(), response.length(), clientAddress, clientPort);
                socket.send(sendPacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}


---client

import java.net.*;
import java.util.Scanner;

public class client {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        Scanner scanner = new Scanner(System.in);

        try {
            // Create a DatagramSocket
            socket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName("localhost");
            int serverPort = 9876;

            while (true) {
                // Get message from user input
                System.out.print("Enter message to send to the server (type 'exit' to quit): ");
                String message = scanner.nextLine();

                // If the user types 'exit', break the loop
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }

                // Send the message to the server
                DatagramPacket sendPacket = new DatagramPacket(message.getBytes(), message.length(), serverAddress, serverPort);
                socket.send(sendPacket);

                // Receive the server's response
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);
                String response = new String(receivePacket.getData(), 0, receivePacket.getLength());

                // Display the server's response
                System.out.println("Server Response: " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            scanner.close();
        }
    }
}

