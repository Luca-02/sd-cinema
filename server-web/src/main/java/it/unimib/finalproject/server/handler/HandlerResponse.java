package it.unimib.finalproject.server.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unimib.finalproject.server.entities.*;

import java.util.*;

public class HandlerResponse {

    public static final String excapeDelimiter = "\r\n!#!\r\n";
    public static final String keyValueDelimiter = "!#!";
    public static final String emptyResponseMessage = "(empty)";
    public static final String okResponseMessage = "OK";
    public static final String trueResponseMessage = "(true)";
    public static final String falseResponseMessage = "(false)";

    public static boolean responseIsOk(String[] responseString) {
        if (Objects.equals(responseString[0], okResponseMessage))
            return true;
        else
            return false;
    }

    public static boolean responseIsTrue(String[] responseString) {
        return Objects.equals(responseString[0], trueResponseMessage);
    }

    public static String[] parseResponse(String responseString) {
        return responseString.split(excapeDelimiter);
    }

    /* - */

    public static Film parseResponseFilm(ObjectMapper mapper, String[] responseString)
            throws JsonProcessingException {
        return parseObjectResponse(mapper, responseString, Film.class);
    }

    public static List<Film> parseResponseFilmList(ObjectMapper mapper, String[] responseString)
            throws JsonProcessingException {
        return parseSingleListResponse(mapper, responseString, Film.class);
    }

    /* - */

    public static Sala parseResponseSala(ObjectMapper mapper, String[] responseString)
            throws JsonProcessingException {
        return parseObjectResponse(mapper, responseString, Sala.class);
    }

    public static List<Sala> parseResponseSalaList(ObjectMapper mapper, String[] responseString)
            throws JsonProcessingException {
        return parseSingleListResponse(mapper, responseString, Sala.class);
    }

    /* - */

    public static Proiezione parseResponseProiezione(ObjectMapper mapper, String[] responseString)
            throws JsonProcessingException {
        return parseObjectResponse(mapper, responseString, Proiezione.class);
    }

    public static List<Proiezione> parseResponseProiezioneList(ObjectMapper mapper, String[] responseString)
            throws JsonProcessingException {
        return parseSingleListResponse(mapper, responseString, Proiezione.class);
    }

    /* - */

    public static List<Prenotazione> parseResponsePrenotazioneList(ObjectMapper mapper, String[] responseString)
            throws JsonProcessingException {
        Map<Integer, Prenotazione> prenotazioniMap = new HashMap<>();
        Map<Integer, List<Posto>> prenotazionePosti = new HashMap<>();
        if (!Objects.equals(responseString[0], emptyResponseMessage)) {
            for (String str : responseString) {
                String[] arr = str.split(keyValueDelimiter);
                try {
                    Posto pos = mapper.readValue(arr[1], Posto.class);
                    Integer idPrenotazione = getIdFromKey(arr[0], 1);

                    if (prenotazionePosti.get(idPrenotazione) == null) {
                        List<Posto> postoList = new ArrayList<>();
                        postoList.add(pos);
                        prenotazionePosti.put(idPrenotazione, postoList);
                    }
                    else {
                        prenotazionePosti.get(idPrenotazione).add(pos);
                    }
                } catch (JsonProcessingException e1) {
                    Prenotazione pre = mapper.readValue(arr[1], Prenotazione.class);
                    pre.setPosti(new ArrayList<>());
                    prenotazioniMap.put(pre.getId(), pre);
                }
            }
        }

        for (var entry : prenotazionePosti.entrySet()) {
            prenotazioniMap.get(entry.getKey()).getPosti().addAll(entry.getValue());
        }

        return new ArrayList<>(prenotazioniMap.values());
    }

    /* - */

    public static List<Posto> parseResponsePostoList(ObjectMapper mapper, String[] responseString)
            throws JsonProcessingException {
        Map<Integer, Posto> postiMap = new HashMap<>();
        if (!Objects.equals(responseString[0], emptyResponseMessage)) {
            for (String str : responseString) {
                String[] arr = str.split(keyValueDelimiter);
                Posto pos = mapper.readValue(arr[1], Posto.class);
                postiMap.put(pos.getId(), pos);
            }
        }

        return new ArrayList<>(postiMap.values());
    }

    /* - */

    public static <T> T parseObjectResponse(ObjectMapper mapper, String[] responseString, Class<T> type)
            throws JsonProcessingException {
        T object = null;
        if (!responseString[0].equals(emptyResponseMessage)) {
            String[] arr = responseString[0].split(keyValueDelimiter);
            object = mapper.readValue(arr[1], type);
        }
        return object;
    }

    public static <T> List<T> parseSingleListResponse(ObjectMapper mapper, String[] responseString, Class<T> type)
            throws JsonProcessingException {
        List<T> objectList = new ArrayList<>();
        if (!Objects.equals(responseString[0], emptyResponseMessage)) {
            for (String str : responseString) {
                String[] arr = str.split(keyValueDelimiter);
                objectList.add(mapper.readValue(arr[1], type));
            }
        }
        return objectList;
    }

    /* - */

    public static Integer getIdFromKey(String str, int index) {
        String[] tokens = str.split(":");
        return Integer.parseInt(tokens[index]);
    }

}
