package it.unimib.finalproject.server.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
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
            // create object mapper instance
            ObjectMapper mapper = new ObjectMapper();

            File path = Paths.get("database.json").toFile();

            // convert JSON file to map
            Map<String, String> map = mapper.readValue(path, Map.class);

            // print map entries
            for (Map.Entry<String, String> entry : map.entrySet()) {
                System.out.println(entry.getKey() + " - " + entry.getValue());
            }

            System.out.println(map.size());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
