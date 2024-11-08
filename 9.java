---server '

import java.io.*;
import java.net.*;

public class server {
    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(6789)) {
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            String fileName = new String(receivePacket.getData(), 0, receivePacket.getLength());
            File file = new File(fileName);
            InetAddress clientAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();

            if (file.exists()) {
                byte[] fileData = new byte[(int) file.length()];
                try (FileInputStream fileInputStream = new FileInputStream(file)) {
                    fileInputStream.read(fileData);
                }
                for (int i = 0; i < fileData.length; i += 1024) {
                    int size = Math.min(1024, fileData.length - i);
                    socket.send(new DatagramPacket(fileData, size, clientAddress, clientPort));
                }
                System.out.println("File sent: " + fileName);
            } else {
                socket.send(new DatagramPacket("File not found!".getBytes(), "File not found!".length(), clientAddress, clientPort));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

--- client

import java.io.*;
import java.net.*;

public class client {
    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress serverAddress = InetAddress.getByName("localhost");
            int serverPort = 6789;

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter file name: ");
            String fileName = reader.readLine();

            socket.send(new DatagramPacket(fileName.getBytes(), fileName.length(), serverAddress, serverPort));

            byte[] receiveData = new byte[1024];
            FileOutputStream fileOutputStream = new FileOutputStream("received_" + fileName);
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);
                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                if (message.equals("File not found!")) {
                    System.out.println(message);
                    break;
                }
                fileOutputStream.write(receivePacket.getData(), 0, receivePacket.getLength());
            }
            System.out.println("File received as received_" + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

