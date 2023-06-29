package it.unimib.finalproject.server;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unimib.finalproject.server.entities.*;
import it.unimib.finalproject.server.handler.HandlerResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.*;
import it.unimib.finalproject.server.entities.*;


@Path("api")
public class CinemaResource {

    /**
     * Porta di connessione per il database
     */
    public static final int PORT = 3030;

    /* - */

    private static ObjectMapper mapper = new ObjectMapper();

    private <T extends IEntity> int generateNewId(List<T> entities) {
    	int newId = 0;
        if (entities.size() > 0) {
        	
        	int maxId = 0;
        	
        	for(T entity: entities) {
        		int id = entity.getId();
        		if (id >= maxId)
        			maxId = id;
        	}
        	
        	newId = maxId + 1;
        }
        return newId;
    }
    
    private String[] sendDbRequest(String command)
    		throws InterruptedException, IOException{
        String response = socketRequest(command);
        String[] parsedResponse = HandlerResponse.parseResponse(response);
        
        return parsedResponse;
    }

    private List<Film> dbGetAllFilm()
    		throws InterruptedException, IOException{
        String command = "MSGETALL film:";
        String[] response = sendDbRequest(command);
        List<Film> films = HandlerResponse.parseResponseFilmList(mapper, response);

        Collections.sort(films);

        return films;
    }

    private Film dbGetFilmById(int id)
    		throws InterruptedException, IOException{
        String command = "MGET film:" + id;

        String[] response = sendDbRequest(command);
        return HandlerResponse.parseResponseFilm(mapper, response);
    }

    private Sala dbGetSalaById(int id)
    		throws InterruptedException, IOException{
        String command = "MGET sala:" + id;

        String[] response = sendDbRequest(command);
        Sala sala = HandlerResponse.parseResponseSala(mapper, response);

        return sala;
    }

    private List<Proiezione> dbGetAllProiezioni() 
    		throws InterruptedException, IOException{
        String command = "MSGETALL proiezione:";

        String[] response = sendDbRequest(command);
        List<Proiezione> proiezioni = HandlerResponse.parseResponseProiezioneList(mapper, response);

        Collections.sort(proiezioni);

        return proiezioni;
    }

    private Proiezione dbGetProiezioneById(int idProiezione) 
    		throws InterruptedException, IOException{
        String command = "MSGETALL proiezione:" + idProiezione;

        String[] response = sendDbRequest(command);
        List<Proiezione> proiezione = HandlerResponse.parseResponseProiezioneList(mapper, response);

        if (proiezione.size() != 0){
            return proiezione.get(0);
        }
        else{
            return null;
        }
    }

    private List<Prenotazione> dbGetPrenotazioniByProiezione(int idProiezione)
    		throws InterruptedException, IOException{
        String command = "MSGETALL proiezione:" + idProiezione + ":prenotazione:";

        String[] response = sendDbRequest(command);
        List<Prenotazione> prenotazioni = HandlerResponse.parseResponsePrenotazioneList(mapper, response);

        Collections.sort(prenotazioni);
        return prenotazioni;
    }

    private Prenotazione dbGetPrenotazione(int idProiezione, int idPrenotazione) 
    		throws InterruptedException, IOException{
        String command = "MSGETALL proiezione:" + idProiezione + ":prenotazione:" + idPrenotazione;

        String[] response = sendDbRequest(command);
        List<Prenotazione> prenotazione = HandlerResponse.parseResponsePrenotazioneList(mapper, response);

        if (prenotazione.size() != 0){
            return prenotazione.get(0);
        }
        else{
            return null;
        }
    }

    private List<Posto> dbGetPostiPrenotati(int idProiezione, int idPrenotazione) 
    		throws InterruptedException, IOException{
        String command = "MSGETALL proiezione:" + idProiezione + ":prenotazione:" + idPrenotazione + ":posto:";

        String[] response = sendDbRequest(command);
        List<Posto> posti = HandlerResponse.parseResponsePostoList(mapper, response);

        Collections.sort(posti);
        return posti;
    }
    
    private List<Posto> dbGetPostiPrenotati(int idProiezione) 
    		throws InterruptedException, IOException {
    	
    	Proiezione proiezione = dbGetProiezioneById(idProiezione);
    	List<Prenotazione> prenotazioni = proiezione.getPrenotazioni();
    	List<Posto> postiPrenotati = new ArrayList<Posto>();
    	
    	for(Prenotazione p : prenotazioni) {
    		postiPrenotati.addAll(p.getPosti());
    	}
        return postiPrenotati;
    }
    
    private List<Posto> dbGetAllPosti()
		throws InterruptedException, IOException{
	        String command = "MSGETALL posto:";

	        String[] response = sendDbRequest(command);
	        List<Posto> posti = HandlerResponse.parseResponsePostoList(mapper, response);

	        Collections.sort(posti);

	        return posti;   	
    }
    
    private boolean dbCreatePrenotazione(int idProiezione, Prenotazione prenotazione)
    		throws InterruptedException, IOException {
    	//TODO: manca la gestione della transazione, non ci basta il tempo credo
    	//es. se va in errore la insert di un posto dovrei annullare tutte le insert precedenti.
    	
    	
    	//TODO: ci sarebbe da verificare se i posti sono nella sala?
    	if(!postiDisponibili(idProiezione, prenotazione))
    		return false;
    	
        String command = "MSET proiezione:" + idProiezione +
        		":prenotazione:" + prenotazione.getId() + " " + prenotazione;
        
        String[] response = sendDbRequest(command);
        
        if (!HandlerResponse.responseIsOk(response)) return false;
        
        List<Posto> postiPrenotazione = prenotazione.getPosti();
      
        List<Posto> postiTotali = dbGetAllPosti();
    	int newId = generateNewId(postiTotali);

        for(Posto posto: postiPrenotazione) {
        	posto.setId(newId);
        	
            command = "MSET proiezione:" + idProiezione + ":prenotazione:" + prenotazione.getId() +
            		":posto:" + posto.getId() + " " + posto;
            
            response = sendDbRequest(command);
            
            if(!HandlerResponse.responseIsOk(response))
            	return false;
            
            newId++;
        }
        return true;
    }
    
    private boolean postiDisponibili(int idProiezione, Prenotazione prenotazione)
    		throws InterruptedException, IOException {
    	List<Posto> postiPrenotati = dbGetPostiPrenotati(idProiezione);
    	List<Posto> postiDaAggiungere = prenotazione.getPosti();
 
    	
    	for(Posto p : postiPrenotati) {
    		for(Posto pNew : postiDaAggiungere) {
    			if (pNew.getRow() == p.getRow() &&
    				pNew.getColumn() == p.getColumn())
    				return false;
    		}
    	}
    	return true;
    }

    public String socketRequest(String request)
            throws InterruptedException, IOException {
        ClientChannel clientChannel = new ClientChannel("localhost", PORT, request);
        Thread thread = new Thread(clientChannel);
        thread.start();
        thread.join();
        return clientChannel.getResponse();
    }

    /* - Film - */

    @Path("/film")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFilm() {
        try {
            List<Film> films = dbGetAllFilm(); 

            return Response.ok(films).build();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }
/*
    @Path("/film")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addFilm(String body) {
        try {
            String[] response;
            int newId;
            boolean isSet = false;

            synchronized (this) {
                List<Film> films = dbGetAllFilm(); 

                if (films.size() == 0)
                    newId = 0;
                else
                    newId = films.get(films.size() - 1).getId() + 1;

                var newObj = mapper.readValue(body, Film.class);
                newObj.setId(newId);

                if (newObj.notNullAttributes()) {
                    String command = "MSET film:" + newId + " " + newObj;
                    response =
                            HandlerResponse.parseResponse(socketRequest(command));
                    isSet = true;
                }
            }

            if (!isSet)
                return Response.status(Response.Status.BAD_REQUEST).build();

            if (HandlerResponse.responseIsOk(response)) {
                var uri = new URI("api/film/" + newId);
                return Response.created(uri).build();
            } else {
                return Response.serverError().build();
            }
        } catch (JsonParseException | JsonMappingException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (IOException | URISyntaxException | InterruptedException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }
*/
    
    @Path("/film/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFilm(@PathParam("id") int idFilm) {
        try {
            Film film = dbGetFilmById(idFilm);

            if (film != null)
                return Response.ok(film).build();
            else
                return Response.status(Response.Status.NOT_FOUND).build();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    /* - Sala - */
/*
    @Path("/sala")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSala() {
        String command = "MSGETALL sala:";

        try {
            String[] response =
                    HandlerResponse.parseResponse(socketRequest(command));
            var objectList =
                    HandlerResponse.parseResponseSalaList(mapper, response);

            Collections.sort(objectList);

            return Response.ok(objectList).build();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @Path("/sala")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSala(String body) {
        try {
            String[] response;
            int newId;
            boolean isSet = false;

            String command = "MSGETALL sala:";

            synchronized (this) {
                response = HandlerResponse.parseResponse(socketRequest(command));
                var objectList =
                        HandlerResponse.parseResponseSalaList(mapper, response);

                Collections.sort(objectList);

                if (objectList.size() == 0)
                    newId = 0;
                else
                    newId = objectList.get(objectList.size() - 1).getId() + 1;

                var newObj = mapper.readValue(body, Sala.class);
                newObj.setId(newId);

                if (newObj.notNullAttributes()) {
                    command = "MSET sala:" + newId + " " + newObj;
                    response =
                            HandlerResponse.parseResponse(socketRequest(command));
                    isSet = true;
                }
            }

            if (!isSet)
                return Response.status(Response.Status.BAD_REQUEST).build();

            if (HandlerResponse.responseIsOk(response)) {
                var uri = new URI("api/sala/" + newId);
                return Response.created(uri).build();
            } else {
                return Response.serverError().build();
            }
        } catch (JsonParseException | JsonMappingException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (IOException | URISyntaxException | InterruptedException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }
*/
    
    @Path("/sala/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSala(@PathParam("id") int idSala) {
        try {
            Sala sala = dbGetSalaById(idSala);

            if (sala != null)
                return Response.ok(sala).build();
            else
                return Response.status(Response.Status.NOT_FOUND).build();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    /* - Proiezione - */

    @Path("/proiezione")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProiezione() {
        try {
            List<Proiezione> proiezioni = dbGetAllProiezioni();

            return Response.ok(proiezioni).build();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }
/*
    @Path("/proiezione")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProiezione(String body) {
        try {
            String[] response;
            int newId;
            boolean isSet = false;

            String command = "MSGETALL proiezione:";

            synchronized (this) {
                response = HandlerResponse.parseResponse(socketRequest(command));
                var proiezioneList =
                        HandlerResponse.parseResponseProiezioneList(mapper, response);

                Collections.sort(proiezioneList);

                if (proiezioneList.size() == 0)
                    newId = 0;
                else
                    newId = proiezioneList.get(proiezioneList.size() - 1).getId() + 1;

                var newObj = mapper.readValue(body, Proiezione.class);
                newObj.setId(newId);
                newObj.setPrenotazioni(new ArrayList<>());

                command = "MEXISTS film:" + newObj.getIdFilm();
                response =
                        HandlerResponse.parseResponse(socketRequest(command));
                if (!HandlerResponse.responseIsTrue(response)) {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }

                command = "MEXISTS sala:" + newObj.getIdSala();
                response =
                        HandlerResponse.parseResponse(socketRequest(command));
                if (!HandlerResponse.responseIsTrue(response)) {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }

                command = "MSGETALL film:";
                response =
                        HandlerResponse.parseResponse(socketRequest(command));
                var filmList =
                        HandlerResponse.parseResponseFilmList(mapper, response);

                if (newObj.notNullAttributes() &&
                        newObj.correctDateTimeFormat() &&
                        !newObj.proiezioneSovrapposta(proiezioneList, filmList)) {
                    command = "MSET proiezione:" + newId + " " + newObj;
                    response =
                            HandlerResponse.parseResponse(socketRequest(command));
                    isSet = true;
                }
            }

            if (!isSet)
                return Response.status(Response.Status.BAD_REQUEST).build();

            if (HandlerResponse.responseIsOk(response)) {
                var uri = new URI("api/proiezione/" + newId);
                return Response.created(uri).build();
            } else {
                return Response.serverError().build();
            }
        } catch (JsonParseException | JsonMappingException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (IOException | URISyntaxException | InterruptedException | ParseException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }
*/
    @Path("/proiezione/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProiezione(@PathParam("id") int idProiezione) {
        try {
            Proiezione proiezione = dbGetProiezioneById(idProiezione);

            if (proiezione != null)
                return Response.ok(proiezione).build();
            else
                return Response.status(Response.Status.NOT_FOUND).build();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    /* - */

    @Path("/proiezione/{id}/prenotazione")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrenotazione(@PathParam("id") int idProiezione) {
        try {
        	if(dbGetProiezioneById(idProiezione) == null)
                return Response.status(Response.Status.NOT_FOUND).build();
        	
            List<Prenotazione> prenotazioni = dbGetPrenotazioniByProiezione(idProiezione);

            return Response.ok(prenotazioni).build();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @Path("/proiezione/{id}/prenotazione")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPrenotazione(@PathParam("id") int idProiezione,
                                    String body) {
        try {
            synchronized (this) {
            	
            	if(dbGetProiezioneById(idProiezione) == null)
                    return Response.status(Response.Status.NOT_FOUND).build();
            	
            	
            	List<Prenotazione> prenotazioni = dbGetPrenotazioniByProiezione(idProiezione);
            	int newId = generateNewId(prenotazioni);
            	
                Prenotazione prenotazione = mapper.readValue(body, Prenotazione.class);
                prenotazione.setId(newId);
                
                if(dbCreatePrenotazione(idProiezione, prenotazione)) {
                    URI uri = new URI("api/proiezione/" + idProiezione + "/prenotazione/" + newId);
                    return Response.created(uri).build();
                }
                else {
                    return Response.serverError().build();
                }
            }
        } catch (JsonParseException | JsonMappingException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (IOException | URISyntaxException | InterruptedException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @Path("/proiezione/{id}/prenotazione/{id2}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrenotazione(@PathParam("id") int idProiezione,
                                    @PathParam("id2") int idPrenotazione) {
        try {
            Prenotazione prenotazione = dbGetPrenotazione(idProiezione, idPrenotazione);

            if (prenotazione != null)
                return Response.ok(prenotazione).build();
            else
                return Response.status(Response.Status.NOT_FOUND).build();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @Path("/proiezione/{id}/prenotazione/{id2}")
    @DELETE
    public Response deletePrenotazione(@PathParam("id") int idProiezione,
                                       @PathParam("id2") int idPrenotazione) {
    	return null;
    }

    /* - */

    @Path("/proiezione/{id}/prenotazione/{id2}/posto")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPosto(@PathParam("id") int idProiezione,
                             @PathParam("id2") int idPrenotazione) {
        try {
            List<Posto> posti = dbGetPostiPrenotati(idProiezione, idPrenotazione);

            return Response.ok(posti).build();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @Path("/proiezione/{id}/prenotazione/{id2}/posto/{id3}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deletePostoPrenotazione(@PathParam("id") int idProiezione,
                                            @PathParam("id2") int idPrenotazione,
                                            @PathParam("id3") int idPosto) {
        return null;
    }

}
