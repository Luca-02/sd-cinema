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
        if (Objects.equals(responseString[0], trueResponseMessage))
            return true;
        else
            return false;
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

    public static List<Proiezione> parseResponseProiezioneList(ObjectMapper mapper, String[] responseString)
            throws JsonProcessingException {
        Map<Integer, Proiezione> proiezioniMap = new HashMap<>();
        Map<Integer, Prenotazione> prenotazioniMap = new HashMap<>();
        Map<Integer, Posto> postiMap = new HashMap<>();
        Map<Integer, Integer> prenotazioniProiezioni = new HashMap<>();
        Map<Integer, Integer> postiPrenotazioni = new HashMap<>();
        if (!Objects.equals(responseString[0], emptyResponseMessage)) {
            for (String str : responseString) {
                String[] arr = str.split(keyValueDelimiter);
                try {
                    Posto pos = mapper.readValue(arr[1], Posto.class);
                    Integer idPrenotazione = getIdFromKey(arr[0], 3);
                    postiMap.put(pos.getId(), pos);
                    postiPrenotazioni.put(pos.getId(), idPrenotazione);
                } catch (JsonProcessingException e1) {
                    try {
                        Prenotazione pre = mapper.readValue(arr[1], Prenotazione.class);
                        pre.setPosti(new ArrayList<>());
                        Integer idProiezione = getIdFromKey(arr[0], 1);
                        prenotazioniMap.put(pre.getId(), pre);
                        prenotazioniProiezioni.put(pre.getId(), idProiezione);
                    } catch (JsonProcessingException e2) {
                        Proiezione pro = mapper.readValue(arr[1], Proiezione.class);
                        pro.setPrenotazioni(new ArrayList<>());
                        proiezioniMap.put(pro.getId(), pro);
                    }
                }
            }
        }

        for (var entry : postiPrenotazioni.entrySet()) {
            prenotazioniMap.get(entry.getValue()).getPosti().add(postiMap.get(entry.getKey()));
        }

        for (var entry : prenotazioniProiezioni.entrySet()) {
            proiezioniMap.get(entry.getValue()).getPrenotazioni().add(prenotazioniMap.get(entry.getKey()));
        }

        return new ArrayList<>(proiezioniMap.values());
    }

    /* - */

    public static List<Prenotazione> parseResponsePrenotazioneList(ObjectMapper mapper, String[] responseString)
            throws JsonProcessingException {
        Map<Integer, Prenotazione> prenotazioniMap = new HashMap<>();
        Map<Integer, Posto> postiMap = new HashMap<>();
        Map<Integer, Integer> postiPrenotazioni = new HashMap<>();
        if (!Objects.equals(responseString[0], emptyResponseMessage)) {
            for (String str : responseString) {
                String[] arr = str.split(keyValueDelimiter);
                try {
                    Posto pos = mapper.readValue(arr[1], Posto.class);
                    Integer idPrenotazione = getIdFromKey(arr[0], 3);
                    postiMap.put(pos.getId(), pos);
                    postiPrenotazioni.put(pos.getId(), idPrenotazione);
                } catch (JsonProcessingException e1) {
                    Prenotazione pre = mapper.readValue(arr[1], Prenotazione.class);
                    pre.setPosti(new ArrayList<>());
                    prenotazioniMap.put(pre.getId(), pre);
                }
            }
        }

        for (var entry : postiPrenotazioni.entrySet()) {
            prenotazioniMap.get(entry.getValue()).getPosti().add(postiMap.get(entry.getKey()));
        }

        return new ArrayList<>(prenotazioniMap.values());
    }

    /* - */

    public static List<Posto> parseResponsePostoList(ObjectMapper mapper, String[] responseString)
            throws JsonProcessingException {
        Map<Integer, Posto> postiMap = new HashMap<>();
        if (!Objects.equals(responseString[0], emptyResponseMessage)) {
            for (String str : responseString) {
                Posto pos = mapper.readValue(str, Posto.class);
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
