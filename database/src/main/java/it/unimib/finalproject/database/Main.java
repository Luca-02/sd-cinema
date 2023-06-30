package it.unimib.finalproject.database;

import it.unimib.finalproject.database.handler.HandlerDatabase;
import it.unimib.finalproject.database.handler.HandlerRequest;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Main {

    /**
     * Porta di ascolto.
     */
    public static final String HOSTNAME = "localhost";
    public static final int PORT = 3030;

    public static final int BUFFER_SIZE = 1024;

    public static ConcurrentHashMap<String, String> database = new ConcurrentHashMap<>();

    private static boolean listen = false;
    private static Selector selector;
    private static InetSocketAddress listenAddress;

    /**
     * Popolo il database
     */
    public static void populateDatabase() throws IOException {
        String file = "../database.json";
        database.putAll(HandlerDatabase.initDatabase(file));
    }

    public static void startServer(String address, Integer port) throws IOException {
        listenAddress = new InetSocketAddress(address, port);

        selector = Selector.open();
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        // set the channel in non-blocking mode
        serverSocket.configureBlocking(false);

        serverSocket.socket().bind(listenAddress);

        // register the channel with the selector
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);

        listen = true;
        System.out.println("Database listening at localhost: " + PORT);

        serverChannelLoop();
    }

    public static void serverChannelLoop() throws IOException {
        while (listen) {
            // wait for events
            int readyCount = selector.select();
            if (readyCount == 0) {
                continue;
            }

            // process selected keys
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();

                // Remove key from set, so we don't process it twice
                iterator.remove();
                if (!key.isValid()) {
                    continue;
                }

                if (key.isAcceptable())
                    accept(key);
                else if (key.isReadable())
                    read(key);
                else if (key.isWritable())
                    write(key);
            }
        }
    }

    // accept client connection
    public static void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();

        SocketChannel clientSocket = serverChannel.accept();
        clientSocket.configureBlocking(false);
        clientSocket.register(selector, SelectionKey.OP_READ);

        System.out.println("Connection Accepted: " + clientSocket.socket().getRemoteSocketAddress());
    }

    // read from the socket channel
    private static void read(SelectionKey key) throws IOException {
        SocketChannel clientSocket = (SocketChannel) key.channel();

        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        StringBuilder receivedData = new StringBuilder();

        try {
            int bytesRead;
            while ((bytesRead = clientSocket.read(buffer)) > 0) {
                buffer.flip();
                String receivedString = new String(buffer.array(), 0, buffer.limit());
                receivedData.append(receivedString);
                buffer.clear();
            }

            if (bytesRead == -1) {
                closeClientConnection(key, clientSocket);
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String request = receivedData.toString();
        buffer.clear();

        System.out.println("[Client: " + clientSocket.socket().getRemoteSocketAddress() +
                ", Received: " + request +
                ", Time: " + LocalDateTime.now() + "]");

        String response = HandlerRequest.handle(request);
        clientSocket.register(selector, SelectionKey.OP_WRITE, response);
    }

    public static void write(SelectionKey key) throws IOException {
        String response = (String) key.attachment();
        SocketChannel clientSocket = (SocketChannel) key.channel();

        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

        int bytesSent = 0;
        while (bytesSent < response.length()) {
            int bytesToWrite = Math.min(BUFFER_SIZE, response.length() - bytesSent);
            buffer.clear();
            buffer.put(response.getBytes(), bytesSent, bytesToWrite);
            buffer.flip();

            while (buffer.hasRemaining()) {
                clientSocket.write(buffer);
            }

            bytesSent += bytesToWrite;
        }

        buffer.clear();
        closeClientConnection(key, clientSocket);
    }

    public static void closeClientConnection(SelectionKey key, SocketChannel clientSocket) throws IOException {
        clientSocket.close();
        key.cancel();
        System.out.println("Connection closed to client: " + clientSocket.socket().getRemoteSocketAddress());
    }

    public static void main(String[] args) {
        try {
            populateDatabase();
            startServer(HOSTNAME, PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
