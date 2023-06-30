package it.unimib.finalproject.server.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Test {

    public static void main(String[] args) throws ParseException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String file = "database.json";

            String json = new String(Files.readAllBytes(Paths.get(file)));

            // convert JSON string to Map
            Map<String, String> map = mapper.readValue(json, Map.class);

            System.out.println(map.size());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
