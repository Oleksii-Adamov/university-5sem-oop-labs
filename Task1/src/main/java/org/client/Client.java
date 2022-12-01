package org.client;

import org.entities.TestEntity;

import java.io.*;
import java.net.Socket;

public class Client {

    private static TestEntity testEntity = new TestEntity("test", 10);

    public static void main(String[] args) {
        Socket socket = null;
        try {
            System.out.println("Connecting to server...");
            socket = new Socket("localhost", 12345);
            System.out.println("Connected");
            ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Client sending " + testEntity);
            toServer.writeObject(testEntity);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
