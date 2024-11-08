---server

import java.io.*;
import java.net.*;

public class server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(6789);
             Socket clientSocket = serverSocket.accept();
             DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream())) {
            
            String fileName = inputStream.readUTF();
            File file = new File(fileName);
            
            if (file.exists()) {
                outputStream.writeLong(file.length());
                try (FileInputStream fileInputStream = new FileInputStream(file)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
                System.out.println("File sent: " + fileName);
            } else {
                outputStream.writeLong(0); // Indicate file not found
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
        try (Socket socket = new Socket("localhost", 6789);
             DataInputStream inputStream = new DataInputStream(socket.getInputStream());
             DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter the file name to download: ");
            String fileName = reader.readLine();
            outputStream.writeUTF(fileName);

            long fileSize = inputStream.readLong();
            if (fileSize > 0) {
                try (FileOutputStream fileOutputStream = new FileOutputStream("received_" + fileName)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead);
                    }
                }
                System.out.println("File received as received_" + fileName);
            } else {
                System.out.println("File not found on server.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

