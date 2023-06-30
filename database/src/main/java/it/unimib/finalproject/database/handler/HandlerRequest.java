package it.unimib.finalproject.database.handler;

import it.unimib.finalproject.database.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HandlerRequest {

    public static final String excapeDelimiter = "\r\n!#!\r\n";
    public static final String keyValueDelimiter = "!#!";
    public static final String emptyResponseMessage = "(empty)";
    public static final String okResponseMessage = "OK";
    public static final String trueResponseMessage = "(true)";
    public static final String falseResponseMessage = "(false)";
    public static final String errorResponseMessage = "(false)";

    public static String handle(String request) {
        String[] split_request = request.split(" ", 2);

        String commands = split_request[0].trim();
        String parameters = split_request[1].trim();

        String response = null;

        switch (commands) {
            case "MGET":
                response = handleMGET(parameters);
                break;
            case "MSGETALL":
                response = handleMSGETALL(parameters);
                break;
            case "MSET":
                response = handleMSET(parameters);
                break;
            case "MDEL":
                response = handleMDEL(parameters);
                break;
            case "MSDEL":
                response = handleMSDEL(parameters);
                break;
            case "MEXISTS":
                response = handleMEXISTS(parameters);
                break;
            default:
                response = errorResponseMessage;
                break;
        }

        return response;
    }

    public static String handleMGET(String parameters) {
        String response = Main.database.getOrDefault(parameters, emptyResponseMessage);
        if (!Objects.equals(response, emptyResponseMessage)) {
            response = parameters + keyValueDelimiter + response;
        }
        return response;
    }

    public static String handleMSGETALL(String parameters) {
        List<String> responseList = new ArrayList<>();
        for (Map.Entry<String, String> entry : Main.database.entrySet()) {
            if (entry.getKey().contains(parameters)) {
                responseList.add(entry.getKey() + keyValueDelimiter + entry.getValue());
            }
        }
        return serializeListToString(responseList);
    }

    public static String handleMSET(String parameters) {
        String[] split_parameters = parameters.split(" ", 2);
        String key = split_parameters[0].trim();
        String value = split_parameters[1].trim();
        Main.database.put(key, value);
        return okResponseMessage;
    }

    public static String handleMDEL(String parameters) {
        if (Main.database.remove(parameters) != null)
            return trueResponseMessage;
        else
            return falseResponseMessage;
    }

    public static String handleMSDEL(String parameters) {
        boolean check = false;
        for (Map.Entry<String, String> entry : Main.database.entrySet()) {
            if (entry.getKey().contains(parameters)) {
                String removedField = Main.database.remove(entry.getKey());
                if (removedField != null)
                    check = true;
            }
        }

        if (check)
            return trueResponseMessage;
        else
            return falseResponseMessage;
    }

    public static String handleMEXISTS(String parameters) {
        if (Main.database.containsKey(parameters))
            return trueResponseMessage;
        else
            return falseResponseMessage;
    }

    private static String serializeListToString(List<String> stringList) {
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
