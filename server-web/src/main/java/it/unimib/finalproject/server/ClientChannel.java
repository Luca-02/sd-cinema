package it.unimib.finalproject.server;

import it.unimib.finalproject.database.Film;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class ClientChannel extends Thread {

    InetSocketAddress hostAddress;
    SocketChannel socketChannel;

    public ClientChannel(String hostname, Integer port) throws IOException {
        hostAddress = new InetSocketAddress(hostname, port);
        socketChannel = SocketChannel.open(hostAddress);
        System.out.println("Client connected!");
    }

    public void run() {
        List<String> objectToSend = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            objectToSend.add("ciao");
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(objectToSend);
            objectOutputStream.flush();
            byte[] serializedObject = byteArrayOutputStream.toByteArray();

            ByteBuffer buffer = ByteBuffer.wrap(serializedObject);
            socketChannel.write(buffer);
            buffer.flip();

            int BUFFER_SIZE = 1024;
            buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

            if (socketChannel.read(buffer) == -1) {
                buffer.clear();
                byteArrayOutputStream.close();
                objectOutputStream.close();
                socketChannel.close();
                System.out.println("Connection closed");
                return;
            }

            buffer.flip();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);

            Film receivedObject = null;
            try {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                receivedObject = (Film) objectInputStream.readObject();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            System.out.println(receivedObject);

            buffer.clear();
            byteArrayOutputStream.close();
            objectOutputStream.close();
            socketChannel.close();
            System.out.println("Connection closed");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}