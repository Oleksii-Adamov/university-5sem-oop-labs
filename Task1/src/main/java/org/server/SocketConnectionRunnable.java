package org.server;

import org.entities.TestEntity;

import java.io.*;
import java.net.Socket;

public class SocketConnectionRunnable implements Runnable {
    Socket socket;

    SocketConnectionRunnable(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            System.out.println("Client connected, socket info: " + socket.toString());
            ObjectInputStream fromClient = new ObjectInputStream(socket.getInputStream());
            TestEntity testEntity = (TestEntity) fromClient.readObject();
            System.out.println("Server received " + testEntity);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
