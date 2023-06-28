package it.unimib.finalproject.database;

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
    public static final int PORT = 3030;
    public static final int BUFFER_SIZE = 1024;

    protected static ConcurrentHashMap<String, String> database = new ConcurrentHashMap<>();

    private static boolean listen = false;
    private static Selector selector;
    private static InetSocketAddress listenAddress;

    /**
     * Popolo il database
     */
    static {
        database.put("film:0", "{\"id\": 0, \"film\": \"Il padrino\", \"durataMinuti\": 175}");
        database.put("film:1", "{\"id\": 1, \"film\": \"Il Signore degli Anelli: Il ritorno del re\", \"durataMinuti\": 201}");
        database.put("film:2", "{\"id\": 2, \"film\": \"Pulp Fiction \", \"durataMinuti\": 154}");
        database.put("film:3", "{\"id\": 3, \"film\": \"Fight Club\", \"durataMinuti\": 139}");
        database.put("film:4", "{\"id\": 4, \"film\": \"Titanic\", \"durataMinuti\": 194}");

        database.put("sala:0", "{\"id\": 0, \"nome\": \"A\", \"row\": 8, \"columns\": 8}");
        database.put("sala:1", "{\"id\": 1, \"nome\": \"B\", \"row\": 6, \"columns\": 5}");
        database.put("sala:2", "{\"id\": 2, \"nome\": \"C\", \"row\": 7, \"columns\": 6}");
        database.put("sala:3", "{\"id\": 3, \"nome\": \"D\", \"row\": 4, \"columns\": 8}");
        database.put("sala:4", "{\"id\": 4, \"nome\": \"E\", \"row\": 12, \"columns\": 6}");

        database.put("proiezione:0", "{\"id\": 0, \"idFilm\": 2, \"idSala\": 1, \"data\": \"2022-12-20\", \"orario\": \"21:00\"}");
        database.put("proiezione:1", "{\"id\": 1, \"idFilm\": 0, \"idSala\": 0, \"data\": \"2021-05-18\", \"orario\": \"21:00\"}");
        database.put("proiezione:2", "{\"id\": 2, \"idFilm\": 3, \"idSala\": 3, \"data\": \"2020-02-03\", \"orario\": \"21:00\"}");
        database.put("proiezione:3", "{\"id\": 3, \"idFilm\": 1, \"idSala\": 4, \"data\": \"2023-08-25\", \"orario\": \"21:00\"}");
        database.put("proiezione:4", "{\"id\": 4, \"idFilm\": 4, \"idSala\": 2, \"data\": \"2019-11-04\", \"orario\": \"21:00\"}");

        database.put("proiezione:0:prenotazione:0", "{\"id\": 0, \"data\": \"2023-09-10\", \"orario\": \"20:00\"}");
        database.put("proiezione:0:prenotazione:1", "{\"id\": 1, \"data\": \"2023-10-10\", \"orario\": \"21:00\"}");

        database.put("proiezione:0:prenotazione:0:posto:0", "{\"id\": 0, \"row\": 0, \"column\": 0}");
        database.put("proiezione:0:prenotazione:0:posto:1", "{\"id\": 1, \"row\": 0, \"column\": 1}");
        database.put("proiezione:0:prenotazione:0:posto:2", "{\"id\": 2, \"row\": 0, \"column\": 2}");
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
            startServer("localhost", PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
