package it.unimib.finalproject.server.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unimib.finalproject.server.ClientChannel;
import it.unimib.finalproject.server.entities.*;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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

    public String[] sendDbRequest(String command)
            throws InterruptedException, IOException {
        String response = socketRequest(command);
        return HandlerResponse.parseResponse(response);
    }

    public <T extends IEntity> T parseEntity(String body, Class<T> type)
            throws JsonProcessingException {
        return mapper.readValue(body, type);
    }

    /* - */

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

    /* - */

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

    /* - */

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

    /* - */

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
            throws IOException, InterruptedException, URISyntaxException {
        String command;
        String[] response;

        List<Prenotazione> prenotazioneList = dbGetAllPrenotazione();
        prenotazione.initCurrentDateTime();
        prenotazione.setId(generateNewId(prenotazioneList));

        if (!dbExistsProiezione(prenotazione.getIdProiezione()))
            return Response.status(Response.Status.NOT_FOUND).build();

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

            if (HandlerResponse.responseIsOk(response)) {
                URI uri = new URI("api/prenotazione/" + prenotazione.getId());
                return Response.created(uri).build();
            }
            else {
                return Response.serverError().build();
            }
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    public boolean dbDeletePrenotazione(int id)
            throws IOException, InterruptedException {
        String command = "MDEL prenotazione:" + id;
        String[] response = sendDbRequest(command);
        return HandlerResponse.responseIsTrue(response);
    }

    /* - */

    public boolean dbAddMultiplePosto(Prenotazione prenotazione)
            throws IOException, InterruptedException {
        for (int i = 0; i < prenotazione.getPosti().size(); i++) {
            if (!dbAddPosto(prenotazione.getPosti().get(i), i, prenotazione.getId()))
                return false;
        }
        return true;
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

    /* - */

//    public List<Prenotazione> dbGetPrenotazioniByProiezione(int idProiezione)
//            throws InterruptedException, IOException{
//        String command = "MSGETALL proiezione:" + idProiezione + ":prenotazione:";
//
//        String[] response = sendDbRequest(command);
//        List<Prenotazione> prenotazioni = HandlerResponse.parseResponsePrenotazioneList(mapper, response);
//
//        Collections.sort(prenotazioni);
//        return prenotazioni;
//    }
//
//    public Prenotazione dbGetPrenotazione(int idProiezione, int idPrenotazione)
//            throws InterruptedException, IOException{
//        String command = "MSGETALL proiezione:" + idProiezione + ":prenotazione:" + idPrenotazione;
//
//        String[] response = sendDbRequest(command);
//        List<Prenotazione> prenotazione = HandlerResponse.parseResponsePrenotazioneList(mapper, response);
//
//        if (prenotazione.size() != 0){
//            return prenotazione.get(0);
//        }
//        else{
//            return null;
//        }
//    }

    /* - */

//    public List<Posto> dbGetPostiPrenotati(int idProiezione, int idPrenotazione)
//            throws InterruptedException, IOException{
//        String command = "MSGETALL proiezione:" + idProiezione + ":prenotazione:" + idPrenotazione + ":posto:";
//
//        String[] response = sendDbRequest(command);
//        List<Posto> posti = HandlerResponse.parseResponsePostoList(mapper, response);
//
//        Collections.sort(posti);
//        return posti;
//    }

//    public List<Posto> dbGetPostiPrenotati(int idProiezione)
//            throws InterruptedException, IOException {
//
//        Proiezione proiezione = dbGetProiezioneById(idProiezione);
//        List<Prenotazione> prenotazioni = proiezione.getPrenotazioni();
//        List<Posto> postiPrenotati = new ArrayList<Posto>();
//
//        for(Prenotazione p : prenotazioni) {
//            postiPrenotati.addAll(p.getPosti());
//        }
//        return postiPrenotati;
//    }

//    public List<Posto> dbGetAllPosti()
//            throws InterruptedException, IOException{
//        String command = "MSGETALL posto:";
//
//        String[] response = sendDbRequest(command);
//        List<Posto> posti = HandlerResponse.parseResponsePostoList(mapper, response);
//
//        Collections.sort(posti);
//
//        return posti;
//    }

    /* - */

//    public boolean dbCreatePrenotazione(int idProiezione, Prenotazione prenotazione)
//            throws InterruptedException, IOException {
//        //TODO: manca la gestione della transazione, non ci basta il tempo credo
//        //es. se va in errore la insert di un posto dovrei annullare tutte le insert precedenti.
//
//
//        //TODO: ci sarebbe da verificare se i posti sono nella sala?
//        if(!postiDisponibili(idProiezione, prenotazione))
//            return false;
//
//        String command = "MSET proiezione:" + idProiezione +
//                ":prenotazione:" + prenotazione.getId() + " " + prenotazione;
//
//        String[] response = sendDbRequest(command);
//
//        if (!HandlerResponse.responseIsOk(response)) return false;
//
//        List<Posto> postiPrenotazione = prenotazione.getPosti();
//
//        List<Posto> postiTotali = dbGetAllPosti();
//        int newId = generateNewId(postiTotali);
//
//        for(Posto posto: postiPrenotazione) {
//            posto.setId(newId);
//
//            command = "MSET proiezione:" + idProiezione + ":prenotazione:" + prenotazione.getId() +
//                    ":posto:" + posto.getId() + " " + posto;
//
//            response = sendDbRequest(command);
//
//            if(!HandlerResponse.responseIsOk(response))
//                return false;
//
//            newId++;
//        }
//        return true;
//    }

//    public boolean postiDisponibili(int idProiezione, Prenotazione prenotazione)
//            throws InterruptedException, IOException {
//        List<Posto> postiPrenotati = dbGetPostiPrenotati(idProiezione);
//        List<Posto> postiDaAggiungere = prenotazione.getPosti();
//
//
//        for(Posto p : postiPrenotati) {
//            for(Posto pNew : postiDaAggiungere) {
//                if (Objects.equals(pNew.getRow(), p.getRow()) &&
//                        Objects.equals(pNew.getColumn(), p.getColumn()))
//                    return false;
//            }
//        }
//        return true;
//    }

}
