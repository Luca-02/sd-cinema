package it.unimib.finalproject.server;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unimib.finalproject.server.entities.*;
import it.unimib.finalproject.server.handler.HandlerRequest;
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
    public static final String HOSTNAME = "localhost";
    public static final int PORT = 3030;

    /* - Film - */

    @Path("/film")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFilm() {
        HandlerRequest hr = new HandlerRequest(HOSTNAME, PORT);

        try {
            List<Film> filmList = hr.dbGetAllFilm();
            Collections.sort(filmList);

            return Response.ok(filmList).build();
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
    }
*/
    
    @Path("/film/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFilm(@PathParam("id") int idFilm) {
        HandlerRequest hr = new HandlerRequest(HOSTNAME, PORT);

        try {
            Film film = hr.dbGetFilmById(idFilm);

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

    @Path("/sala")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSala() {
        HandlerRequest hr = new HandlerRequest(HOSTNAME, PORT);

        try {
            List<Sala> salaList = hr.dbGetAllSala();
            Collections.sort(salaList);

            return Response.ok(salaList).build();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

/*
    @Path("/sala")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSala(String body) {
    }
*/
    
    @Path("/sala/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSala(@PathParam("id") int idSala) {
        HandlerRequest hr = new HandlerRequest(HOSTNAME, PORT);

        try {
            Sala sala = hr.dbGetSalaById(idSala);

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
        HandlerRequest hr = new HandlerRequest(HOSTNAME, PORT);

        try {
            List<Proiezione> proiezioneList = hr.dbGetAllProiezione();
            Collections.sort(proiezioneList);

            return Response.ok(proiezioneList).build();
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
    }
*/

    @Path("/proiezione/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProiezione(@PathParam("id") int idProiezione) {
        HandlerRequest hr = new HandlerRequest(HOSTNAME, PORT);

        try {
            Proiezione proiezione = hr.dbGetProiezioneById(idProiezione);

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

    @Path("/prenotazione")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrenotazione(@QueryParam("idProiezione") String idProiezioneStr) {
        HandlerRequest hr = new HandlerRequest(HOSTNAME, PORT);

        try {
            List<Prenotazione> prenotazioneList = hr.dbGetAllPrenotazione();

            if (idProiezioneStr != null) {
                int idProiezione = Integer.parseInt(idProiezioneStr);
                prenotazioneList = hr.dbGetAllPrenotazioneByProiezione(prenotazioneList, idProiezione);
            }
            Collections.sort(prenotazioneList);

            return Response.ok(prenotazioneList).build();
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @Path("/prenotazione")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPrenotazione(String body) {
        HandlerRequest hr = new HandlerRequest(HOSTNAME, PORT);

        try {
            Prenotazione prenotazione = hr.parseEntity(body, Prenotazione.class);
            Response responseCode;

            synchronized (this) {
                responseCode = hr.dbAddPrenotazione(prenotazione);
            }

            return responseCode;
        } catch (JsonParseException | JsonMappingException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (IOException | URISyntaxException | InterruptedException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @Path("/prenotazione/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrenotazione(@PathParam("id") int idPrenotazione) {
        HandlerRequest hr = new HandlerRequest(HOSTNAME, PORT);

        try {
            Prenotazione prenotazione = hr.dbGetPrenotazioneById(idPrenotazione);

            if (prenotazione != null)
                return Response.ok(prenotazione).build();
            else
                return Response.status(Response.Status.NOT_FOUND).build();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @Path("/prenotazione/{id}")
    @DELETE
    public Response deletePrenotazione(@PathParam("id") int idPrenotazione) {
        HandlerRequest hr = new HandlerRequest(HOSTNAME, PORT);

        try {
            boolean check = hr.dbDeletePrenotazione(idPrenotazione);
            if (check)
                return Response.noContent().build();
            else
                return Response.status(Response.Status.NOT_FOUND).build();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    /* - */

    @Path("prenotazione/{id}/posto")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPosto(@PathParam("id") int idPrenotazione) {
//        HandlerRequest hr = new HandlerRequest(HOSTNAME, PORT, mapper);
//
//        try {
//            List<Posto> posti = hr.dbGetPostiPrenotati(idProiezione, idPrenotazione);
//
//            return Response.ok(posti).build();
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//            return Response.serverError().build();
//        }
        return null;
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
