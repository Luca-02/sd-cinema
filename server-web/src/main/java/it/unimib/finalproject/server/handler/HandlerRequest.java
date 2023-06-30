package it.unimib.finalproject.server.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unimib.finalproject.server.ClientChannel;
import it.unimib.finalproject.server.entities.*;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class HandlerRequest {

    private final String hostname;
    private final int port;
    private final ObjectMapper mapper;

    public HandlerRequest(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        this.mapper = new ObjectMapper();
    }

    public String socketRequest(String request)
            throws InterruptedException, IOException {
        ClientChannel clientChannel = new ClientChannel(hostname, port, request);
        Thread thread = new Thread(clientChannel);
        thread.start();
        thread.join();
        return clientChannel.getResponse();
    }


    public String[] sendDbRequest(String command)
            throws InterruptedException, IOException {
        String response = socketRequest(command);
        return HandlerResponse.parseResponse(response);
    }

    public <T extends IEntity> int generateNewId(List<T> entities) {
        int newId = 0;
        if (entities.size() > 0) {
            int maxId = 0;
            for(T entity: entities) {
                if (entity.getId() >= maxId)
                    maxId = entity.getId();
            }
            newId = maxId + 1;
        }
        return newId;
    }

    public <T extends IEntity> T parseEntity(String body, Class<T> type)
            throws JsonProcessingException {
        return mapper.readValue(body, type);
    }

    public Response provideUrl(String[] response, String path)
            throws URISyntaxException {
        if (HandlerResponse.responseIsOk(response)) {
            URI uri = new URI(path);
            return Response.created(uri).build();
        }
        else {
            return Response.serverError().build();
        }
    }

    /* - Film - */

    public List<Film> dbGetAllFilm()
            throws InterruptedException, IOException{
        String command = "MSGETALL film:";

        String[] response = sendDbRequest(command);
        return HandlerResponse.parseResponseFilmList(mapper, response);
    }

    public Film dbGetFilmById(int id)
            throws InterruptedException, IOException{
        String command = "MGET film:" + id;

        String[] response = sendDbRequest(command);
        return HandlerResponse.parseResponseFilm(mapper, response);
    }

    public Response dbAddFilm(Film film)
            throws IOException, InterruptedException, URISyntaxException {
        String command;
        String[] response;

        List<Film> filmList = dbGetAllFilm();
        film.setId(generateNewId(filmList));

        if (film.notNullAttributes()) {
            command = "MSET film:" + film.getId() + " " + film;
            response = sendDbRequest(command);

            return provideUrl(response, "api/film/" + film.getId());
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    public boolean dbExistsFilm(int id)
            throws IOException, InterruptedException {
        String command = "MEXISTS film:" + id;

        String[] response =
                HandlerResponse.parseResponse(socketRequest(command));
        return HandlerResponse.responseIsTrue(response);
    }

    /* - Sala - */

    public List<Sala> dbGetAllSala()
            throws InterruptedException, IOException{
        String command = "MSGETALL sala:";

        String[] response = sendDbRequest(command);
        return HandlerResponse.parseResponseSalaList(mapper, response);
    }

    public Sala dbGetSalaById(int id)
            throws InterruptedException, IOException{
        String command = "MGET sala:" + id;

        String[] response = sendDbRequest(command);
        return HandlerResponse.parseResponseSala(mapper, response);
    }

    public Sala dbGetSalaByPrenotazione(Prenotazione prenotazione)
            throws IOException, InterruptedException {
        Proiezione proiezione = dbGetProiezioneById(prenotazione.getIdProiezione());
        return dbGetSalaById(proiezione.getIdSala());
    }

    public Response dbAddSala(Sala sala)
            throws IOException, InterruptedException, URISyntaxException {
        String command;
        String[] response;

        List<Sala> salaList = dbGetAllSala();
        sala.setId(generateNewId(salaList));

        if (sala.notNullAttributes()) {
            command = "MSET sala:" + sala.getId() + " " + sala;
            response = sendDbRequest(command);

            return provideUrl(response, "api/sala/" + sala.getId());
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    public boolean dbExistsSala(int id)
            throws IOException, InterruptedException {
        String command = "MEXISTS sala:" + id;

        String[] response =
                HandlerResponse.parseResponse(socketRequest(command));
        return HandlerResponse.responseIsTrue(response);
    }

    /* - Proiezione - */

    public List<Proiezione> dbGetAllProiezione()
            throws InterruptedException, IOException {
        String command = "MSGETALL proiezione:";

        String[] response = sendDbRequest(command);
        return HandlerResponse.parseResponseProiezioneList(mapper, response);
    }

    public Proiezione dbGetProiezioneById(int id)
            throws InterruptedException, IOException{
        String command = "MGET proiezione:" + id;

        String[] response = sendDbRequest(command);
        return HandlerResponse.parseResponseProiezione(mapper, response);
    }

    public boolean dbExistsProiezione(int id)
            throws IOException, InterruptedException {
        String command = "MEXISTS proiezione:" + id;

        String[] response =
                HandlerResponse.parseResponse(socketRequest(command));
        return HandlerResponse.responseIsTrue(response);
    }

    public Response dbAddProiezione(Proiezione proiezione)
            throws IOException, InterruptedException, URISyntaxException, ParseException {
        String command;
        String[] response;

        List<Proiezione> proiezioneList = dbGetAllProiezione();
        proiezione.setId(generateNewId(proiezioneList));

        if (!dbExistsFilm(proiezione.getIdFilm()) ||
                !dbExistsSala(proiezione.getIdSala()))
            return Response.status(Response.Status.NOT_FOUND).build();

        if (proiezione.notNullAttributes() &&
                proiezione.correctDateTimeFormat()) {

            if (proiezione.overlapProiezione(proiezioneList, dbGetAllFilm()))
                return Response.status(Response.Status.CONFLICT).build();

            command = "MSET proiezione:" + proiezione.getId() + " " + proiezione;
            response = sendDbRequest(command);

            return provideUrl(response, "api/proiezione/" + proiezione.getId());
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    /* - Prenotazione - */

    public List<Prenotazione> dbGetAllPrenotazione()
            throws IOException, InterruptedException {
        String command = "MSGETALL prenotazione:";

        String[] response = sendDbRequest(command);
        return HandlerResponse.parseResponsePrenotazioneList(mapper, response);
    }

    public List<Prenotazione> dbGetAllPrenotazioneByProiezione(List<Prenotazione> prenotazioneList, int idProiezione) {
        List<Prenotazione> resultList = new ArrayList<>();

        for (Prenotazione prenotazione : prenotazioneList) {
            if (prenotazione.getIdProiezione() == idProiezione) {
                resultList.add(prenotazione);
            }
        }
        return resultList;
    }

    public Prenotazione dbGetPrenotazioneById(int id)
            throws InterruptedException, IOException{
        String command = "MSGETALL prenotazione:" + id;

        String[] response = sendDbRequest(command);
        List<Prenotazione> prenotazioni = HandlerResponse.parseResponsePrenotazioneList(mapper, response);

        if (prenotazioni.size() != 0)
            return prenotazioni.get(0);
        else
            return null;
    }

    public Response dbAddPrenotazione(Prenotazione prenotazione)
            throws IOException, InterruptedException, URISyntaxException, ParseException {
        String command;
        String[] response;

        List<Prenotazione> prenotazioneList = dbGetAllPrenotazione();
        prenotazione.initCurrentDateTime();
        prenotazione.setId(generateNewId(prenotazioneList));

        if (!dbExistsProiezione(prenotazione.getIdProiezione()))
            return Response.status(Response.Status.NOT_FOUND).build();

        if (!prenotazione.validDateTime(
                dbGetProiezioneById(prenotazione.getIdProiezione())))
            return Response.status(Response.Status.BAD_REQUEST).build();

        if (prenotazione.notNullAttributes()) {
            if (prenotazione.getPosti() != null) {
                if (prenotazione.getPosti().size() == 0)
                    return Response.status(Response.Status.BAD_REQUEST).build();

                HandlerPrenotazione hp = new HandlerPrenotazione(
                        dbGetSalaByPrenotazione(prenotazione),
                        prenotazioneList);

                if (hp.available(prenotazione)) {
                    boolean addedPosti = dbAddMultiplePosto(prenotazione);

                    if (!addedPosti)
                        return Response.serverError().build();
                }
                else {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
            }
            else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            command = "MSET prenotazione:" + prenotazione.getId() + " " + prenotazione;
            response = sendDbRequest(command);

            return provideUrl(response, "api/prenotazione/" + prenotazione.getId());
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    public boolean dbDeletePrenotazione(int id)
            throws IOException, InterruptedException {
        String command = "MSDEL prenotazione:" + id;

        String[] response = sendDbRequest(command);
        return HandlerResponse.responseIsTrue(response);
    }

    /* - Posto - */

    public List<Posto> dbGetAllPosto(int idPrenotazione)
            throws IOException, InterruptedException {
        Prenotazione prenotazione = dbGetPrenotazioneById(idPrenotazione);
        return prenotazione.getPosti();
    }

    public boolean dbAddPosto(Posto posto, int idPosto, int idPrenotazione)
            throws IOException, InterruptedException {
        posto.setId(idPosto);
        String key = "prenotazione:" + idPrenotazione + ":posto:" + posto.getId();
        String value = posto.toString();

        String command = "MSET " + key + " " + value;

        String[] response = sendDbRequest(command);
        return HandlerResponse.responseIsOk(response);
    }

    public boolean dbAddMultiplePosto(Prenotazione prenotazione)
            throws IOException, InterruptedException {
        for (int i = 0; i < prenotazione.getPosti().size(); i++) {
            if (!dbAddPosto(prenotazione.getPosti().get(i), i, prenotazione.getId()))
                return false;
        }
        return true;
    }

    public boolean dbDeletePosto(int idPrenotazione, int idPosto)
            throws IOException, InterruptedException {
        String command = "MDEL prenotazione:" + idPrenotazione + ":posto:" + idPosto;

        String[] response = sendDbRequest(command);
        return HandlerResponse.responseIsTrue(response);
    }

}
