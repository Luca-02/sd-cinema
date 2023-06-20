package it.unimib.finalproject.database;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Main {

    /**
     * Porta di ascolto.
     */
    public static final int PORT = 3030;

    private static ConcurrentHashMap<Integer, Film> filmMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Integer, Sala> saleMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Integer, Proiezione> proiezioniMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Integer, Prenotazione> prenotazioniMap = new ConcurrentHashMap<>();

    private static boolean listen = false;
    private Selector selector;
    private InetSocketAddress listenAddress;

    /**
     * Popolo il database
     */
    static {
        Film f = new Film(0, "film1", 120);
        filmMap.put(f.getId(), f);

        Sala s = new Sala(0, "sala1", 3, 3);
        saleMap.put(s.getId(), s);

        Proiezione pro = new Proiezione(0, f, s, "00/00/0000", "12:30");
        proiezioniMap.put(pro.getId(), pro);

        List<Posto> posti = new ArrayList<>();
        posti.add(new Posto(0, 0));
        posti.add(new Posto(0, 1));

        Prenotazione pre = new Prenotazione(0, pro.getId(), posti);
        prenotazioniMap.put(pre.getId(), pre);
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
        ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

        try {
            if (clientSocket.read(buffer) == -1) {
                clientSocket.close();
                key.cancel();
                System.out.println("Connection closed by client: " + clientSocket.socket().getRemoteSocketAddress());
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        buffer.flip();
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);

        List<String> receivedObject = null;
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            receivedObject = (List<String>) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        System.out.println(receivedObject);
        buffer.flip();

        Film objectToSend = filmMap.get(0);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(objectToSend);
        objectOutputStream.flush();
        byte[] serializedObject = byteArrayOutputStream.toByteArray();

        buffer = ByteBuffer.wrap(serializedObject);
        clientSocket.write(buffer);

        buffer.clear();
        byteArrayOutputStream.close();
        objectOutputStream.close();
    }

    public static void main(String[] args) {
        try {
            new Main("localhost", PORT).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
