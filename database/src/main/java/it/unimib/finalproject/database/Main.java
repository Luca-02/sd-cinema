package it.unimib.finalproject.database;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Main {

    /**
     * Porta di ascolto.
     */
    public static final int PORT = 3030;

    protected static ConcurrentHashMap<String, String> database = new ConcurrentHashMap<>();

    private static boolean listen = false;
    private Selector selector;
    private InetSocketAddress listenAddress;

    /**
     * Popolo il database
     */
    static {
        database.put("film:0", "\"{'id': 0, 'nome': 'Il padrino', 'durataMinuti': 175}\"");
        database.put("film:1", "\"{'id': 1, 'nome': 'Schindler's List', 'durataMinuti': 195}\"");
        database.put("film:2", "\"{'id': 2, 'nome': 'Il Signore degli Anelli: Il ritorno del re', 'durataMinuti': 201}\"");
        database.put("film:3", "\"{'id': 3, 'nome': 'Pulp Fiction ', 'durataMinuti': 154}\"");
        database.put("film:4", "\"{'id': 4, 'nome': 'Fight Club', 'durataMinuti': 139}\"");
    }

    public Main(String address, Integer port) throws IOException {
        listenAddress = new InetSocketAddress(address, port);

        selector = Selector.open();
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        // set the channel in non-blocking mode
        serverSocket.configureBlocking(false);

        serverSocket.socket().bind(listenAddress);

        int ops = serverSocket.validOps();
        // register the channel with the selector
        serverSocket.register(selector, ops);

        listen = true;
        System.out.println("Database listening at localhost: " + PORT);
    }

    public void start() throws IOException {
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
            }
        }
    }

    // accept client connection
    public void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();

        SocketChannel clientSocket = serverChannel.accept();
        clientSocket.configureBlocking(false);
        clientSocket.register(selector, SelectionKey.OP_READ);

        System.out.println("Connection Accepted: " + clientSocket.socket().getRemoteSocketAddress());
    }

    // read from the socket channel
    private void read(SelectionKey key) throws IOException {
        SocketChannel clientSocket = (SocketChannel) key.channel();

        int BUFFER_SIZE = 1024;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

        try {
            if (clientSocket.read(buffer) == -1) {
                closeClientConnection(key, clientSocket);
                System.out.println("Connection closed to client: " + clientSocket.socket().getRemoteSocketAddress());
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        buffer.flip();
        CharBuffer charBuffer = StandardCharsets.UTF_8.newDecoder().decode(buffer);
        buffer.clear();

        String request = charBuffer.toString().trim();

        System.out.println("[Client: " + clientSocket.socket().getRemoteSocketAddress() +
                ", Request: " + request +
                ", Time: " + LocalDateTime.now() + "]");

        HandlerRequest.handle(request, clientSocket);

        clientSocket.close();
        key.cancel();
    }

    public void closeClientConnection(SelectionKey key, SocketChannel clientSocket) throws IOException {
        clientSocket.close();
        key.cancel();
        System.out.println("Connection closed to client: " + clientSocket.socket().getRemoteSocketAddress());
    }

    public static void main(String[] args) {
        try {
            new Main("localhost", PORT).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
