package it.unimib.finalproject.server;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import it.unimib.finalproject.server.entities.*;
import it.unimib.finalproject.server.handler.HandlerRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Collections;
import java.util.*;


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

    @Path("/film")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addFilm(String body) {
        HandlerRequest hr = new HandlerRequest(HOSTNAME, PORT);

        try {
            Film film = hr.parseEntity(body, Film.class);
            Response responseCode;

            synchronized (this) {
                responseCode = hr.dbAddFilm(film);
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

    @Path("/sala")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSala(String body) {
        HandlerRequest hr = new HandlerRequest(HOSTNAME, PORT);

        try {
            Sala sala = hr.parseEntity(body, Sala.class);
            Response responseCode;

            synchronized (this) {
                responseCode = hr.dbAddSala(sala);
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


    @Path("/proiezione")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProiezione(String body) {
        HandlerRequest hr = new HandlerRequest(HOSTNAME, PORT);

        try {
            Proiezione proiezione = hr.parseEntity(body, Proiezione.class);
            Response responseCode;

            synchronized (this) {
                responseCode = hr.dbAddProiezione(proiezione);
            }

            return responseCode;
        } catch (JsonParseException | JsonMappingException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (IOException | URISyntaxException | InterruptedException | ParseException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }


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
        } catch (IOException | URISyntaxException | InterruptedException | ParseException e) {
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

    /* - Posto - */

    @Path("prenotazione/{id}/posto")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPosto(@PathParam("id") int idPrenotazione) {
        HandlerRequest hr = new HandlerRequest(HOSTNAME, PORT);

        try {
            List<Posto> posti = hr.dbGetAllPosto(idPrenotazione);
            Collections.sort(posti);

            return Response.ok(posti).build();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @Path("/prenotazione/{id1}/posto/{id2}")
    @DELETE
    public Response deletePostoPrenotazione(@PathParam("id1") int idPrenotazione,
                                            @PathParam("id2") int idPosto) {
        HandlerRequest hr = new HandlerRequest(HOSTNAME, PORT);

        try {
            boolean check = hr.dbDeletePosto(idPrenotazione, idPosto);

            if (check) {
                Prenotazione prenotazione = hr.dbGetPrenotazioneById(idPrenotazione);
                if (prenotazione != null) {
                    if (prenotazione.getPosti().size() == 0) {
                        check = hr.dbDeletePrenotazione(idPrenotazione);
                        if (check)
                            return Response.noContent().build();
                        else
                            return Response.status(Response.Status.NOT_FOUND).build();
                    }
                    else {
                        return Response.noContent().build();
                    }
                }
                else {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
            }
            else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

}
