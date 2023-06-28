package it.unimib.finalproject.server;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;

public class ClientChannel implements Runnable {

    public static final int BUFFER_SIZE = 1024;

    private InetSocketAddress hostAddress;
    private SocketChannel socketChannel;
    private String request;
    private String response;

    public ClientChannel(String hostname, Integer port, String request) throws IOException {
        this.hostAddress = new InetSocketAddress(hostname, port);
        this.socketChannel = SocketChannel.open(hostAddress);
        this.request = request;
        System.out.println("Client connected!");
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public void run() {
        try {
            writeToServer(request);
            setResponse(readFromServer());

            Thread.sleep((int) (10 * Math.random()));
            socketChannel.close();
            System.out.println("Connection closed");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeToServer(String request) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

        int bytesSent = 0;
        while (bytesSent < request.length()) {
            int bytesToWrite = Math.min(BUFFER_SIZE, request.length() - bytesSent);
            buffer.clear();
            buffer.put(request.getBytes(), bytesSent, bytesToWrite);
            buffer.flip();

            while (buffer.hasRemaining()) {
                socketChannel.write(buffer);
            }

            bytesSent += bytesToWrite;
        }

        buffer.clear();

        System.out.println("[Server: " + socketChannel.socket().getRemoteSocketAddress() +
                ", Send: " + request +
                ", Time: " + LocalDateTime.now() + "]");
    }

    public String readFromServer() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        StringBuilder receivedData = new StringBuilder();

        while (socketChannel.read(buffer) != -1) {
            buffer.flip();

            String receivedString = new String(buffer.array(), 0, buffer.limit());
            receivedData.append(receivedString);

            buffer.clear();
        }

//        System.out.println("Received: ");
//        System.out.println(receivedData.toString());

        buffer.clear();
        return receivedData.toString();
    }

}