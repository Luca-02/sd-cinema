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
import java.util.*;

@Path("api")
public class CinemaResource {

    /**
     * Porta di connessione per il database
     */
    public static final int PORT = 3030;

    /* - */

    private static ObjectMapper mapper = new ObjectMapper();

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
        String command = "MSGETALL film:";

        try {
            String[] response =
                    HandlerResponse.parseResponse(socketRequest(command));
            var objectList =
                    HandlerResponse.parseResponseFilmList(mapper, response);

            Collections.sort(objectList);

            return Response.ok(objectList).build();
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
        String command = "MSGETALL film:";

        try {
            String[] response;
            int newId;

            boolean isSet = false;

            synchronized (this) {
                response = HandlerResponse.parseResponse(socketRequest(command));
                var objectList =
                        HandlerResponse.parseResponseFilmList(mapper, response);

                Collections.sort(objectList);

                if (objectList.size() == 0)
                    newId = 0;
                else
                    newId = objectList.get(objectList.size() - 1).getId() + 1;

                var newObj = mapper.readValue(body, Film.class);
                newObj.setId(newId);

                if (newObj.notNullAttributes()) {
                    command = "MSET film:" + newId + " " + newObj;
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

    @Path("/film/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFilm(@PathParam("id") int idFilm) {
        String command = "MGET film:" + idFilm;

        try {
            String[] response =
                    HandlerResponse.parseResponse(socketRequest(command));
            var object =
                    HandlerResponse.parseResponseFilm(mapper, response);

            if (object != null)
                return Response.ok(object).build();
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
        String command = "MSGETALL sala:";

        try {
            String[] response;
            int newId;

            boolean isSet = false;

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

    @Path("/sala/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSala(@PathParam("id") int idSala) {
        String command = "MGET sala:" + idSala;

        try {
            String[] response =
                    HandlerResponse.parseResponse(socketRequest(command));
            var object =
                    HandlerResponse.parseResponseSala(mapper, response);

            if (object != null)
                return Response.ok(object).build();
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
        String command = "MSGETALL proiezione:";

        try {
            String[] response =
                    HandlerResponse.parseResponse(socketRequest(command));
            var objectList =
                    HandlerResponse.parseResponseProiezioneList(mapper, response);

            Collections.sort(objectList);

            return Response.ok(objectList).build();
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
        String command = "MSGETALL proiezione:";

        try {
            String[] response;
            int newId;

            boolean isSet = false;

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

                Map<Integer, Film> filmMap = new HashMap<>();
                for (var obj : filmList) {
                    filmMap.put(obj.getId(), obj);
                }

                if (newObj.notNullAttributes() && !newObj.proiezioneSovrapposta(proiezioneList, filmMap)) {
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

    @Path("/proiezione/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProiezione(@PathParam("id") int idProiezione) {
        String command = "MSGETALL proiezione:" + idProiezione;

        try {
            String[] response =
                    HandlerResponse.parseResponse(socketRequest(command));
            var objectList =
                    HandlerResponse.parseResponseProiezioneList(mapper, response);

            if (objectList.size() != 0)
                return Response.ok(objectList.get(0)).build();
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
        String command = "MSGETALL proiezione:" + idProiezione + ":prenotazione:";

        try {
            String[] response =
                    HandlerResponse.parseResponse(socketRequest(command));
            var objectList =
                    HandlerResponse.parseResponsePrenotazioneList(mapper, response);

            Collections.sort(objectList);

            return Response.ok(objectList).build();
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
        return null;
    }

    @Path("/proiezione/{id}/prenotazione/{id2}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrenotazione(@PathParam("id") int idProiezione,
                                    @PathParam("id2") int idPrenotazione) {
        String command = "MSGETALL proiezione:" + idProiezione + ":prenotazione:" + idPrenotazione;

        try {
            String[] response =
                    HandlerResponse.parseResponse(socketRequest(command));
            var objectList =
                    HandlerResponse.parseResponsePrenotazioneList(mapper, response);

            if (objectList.size() != 0)
                return Response.ok(objectList.get(0)).build();
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
        String command = "MSGETALL proiezione:" + idProiezione + ":prenotazione:" + idPrenotazione + ":posto:";

        try {
            String[] response =
                    HandlerResponse.parseResponse(socketRequest(command));
            var objectList =
                    HandlerResponse.parseResponsePostoList(mapper, response);

            Collections.sort(objectList);

            return Response.ok(objectList).build();
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
