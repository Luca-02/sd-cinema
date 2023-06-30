package it.unimib.finalproject.database.handler;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class HandlerDatabase {

    public static Map<String, String> initDatabase(String pathString)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = new String(Files.readAllBytes(Paths.get(pathString)));
        return mapper.readValue(json, Map.class);
    }

}
