package it.unimib.finalproject.database;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HandlerRequest {

    public static final String excapeDelimiter = "\r\n!#!\r\n";
    public static final String emptyResponseMessage = "(empty)";

    public static void handle(String request, SocketChannel clientSocket) throws IOException {
        String[] split_request = request.split(" ", 2);

        String commands = split_request[0].trim();
        String parameters = split_request[1].trim();

        switch (commands) {
            case "HSUBGETALL":
                handleHSUBGETALL(parameters, clientSocket);
                break;
            default:
                break;
        }
    }

    public static void handleHSUBGETALL(String parameters, SocketChannel clientSocket) throws IOException {
        List<String> responseList = new ArrayList<>();
        for (Map.Entry<String, String> entry : Main.database.entrySet()) {
            if (entry.getKey().contains(parameters)) {
                responseList.add(entry.getValue());
            }
        }

        String serializedData = serializeToStringList(responseList);

        System.out.println(serializedData);

        int BUFFER_SIZE = 1024;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

        serializedData = "ok";

        buffer.put(serializedData.getBytes());
        buffer.flip();

        clientSocket.write(buffer);

        buffer.clear();
    }

    private static String serializeToStringList(List<String> stringList) {
        StringBuilder result = new StringBuilder();
        if (stringList.size() == 0) {
            result = new StringBuilder(emptyResponseMessage);
        }
        else {
            for (String str : stringList) {
                result.append(str).append(excapeDelimiter);
            }
        }
        return result.toString();
    }

}
