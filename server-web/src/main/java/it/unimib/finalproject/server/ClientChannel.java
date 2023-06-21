package it.unimib.finalproject.server;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientChannel extends Thread {

    private InetSocketAddress hostAddress;
    private SocketChannel socketChannel;

    private String request;

    public ClientChannel(String hostname, Integer port, String request) throws IOException {
        this.hostAddress = new InetSocketAddress(hostname, port);
        this.socketChannel = SocketChannel.open(hostAddress);
        this.request = request;
        System.out.println("Client connected!");
    }

    public void run() {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put(request.getBytes());
            buffer.flip();

            socketChannel.write(buffer);
            buffer.clear();

            buffer = ByteBuffer.allocate(1024);
            StringBuilder receivedData = new StringBuilder();

            while (socketChannel.read(buffer) != -1) {
                buffer.flip();

                String receivedString = new String(buffer.array(), 0, buffer.limit());
                receivedData.append(receivedString);

                buffer.clear();
            }

            System.out.println("Received: ");
            String receivedDataString = receivedData.toString();
            System.out.println(receivedDataString);

            sleep((int) (500 * Math.random()));
            socketChannel.close();
            System.out.println("Connection closed");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}