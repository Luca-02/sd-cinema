package it.unimib.finalproject.server;

import it.unimib.finalproject.database.Command;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.io.*;

@Path("cinema/api")
public class CinemaResource {

    /**
     * Porta di ascolto.
     */
    public static final int PORT = 3030;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTest() throws InterruptedException {
        ClientChannel c = null;
        try {
            c = new ClientChannel("localhost", PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert c != null;
        c.start();

        return Response.status(Response.Status.ACCEPTED).build();
    }

    /* - */

    @Path("/film")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFilm() {
        Command command = new Command(Command.Request.GET, "/film");

        return null;
    }

    @Path("/film")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addFilm(String body) {
        return null;
    }

    @Path("/film/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFilm(@PathParam("id") int id) {
        return null;
    }

    /* - */

    @Path("/sala")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSala() {
        return null;
    }

    @Path("/sala")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSala(String body) {
        return null;
    }

    @Path("/sala/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSala(@PathParam("id") int id) {
        return null;
    }

    /* - */

    @Path("/proiezione")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProiezione() {
        return null;
    }

    @Path("/proiezione")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProiezione(String body) {
        return null;
    }

    @Path("/proiezione/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProiezione(@PathParam("id") int id) {
        return null;
    }

    @Path("/proiezione/{id}/prenotazione")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrenotazione(@PathParam("id") int idProiezione) {
        return null;
    }

    @Path("/proiezione/{id}/prenotazione/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrenotazione(@PathParam("id") int idProiezione,
                                    @PathParam("id") int idPrenotazione) {
        return null;
    }

    @Path("/proiezione/{id}/prenotazione")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPrenotazione(@PathParam("id") int idProiezione,
                                    String body) {

        return null;
    }

    @Path("/proiezione/{id}/prenotazione/{id}")
    @DELETE
    public Response deletePrenotazione(@PathParam("id") int idProiezione,
                                       @PathParam("id") int idPrenotazione) {
        return null;
    }

    @Path("/proiezione/{id}/prenotazione/{id}/posto/{id}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deletePostoPrenotazione(@PathParam("id") int idProiezione,
                                            @PathParam("id") int idPrenotazione,
                                            @PathParam("id") int idPosto) {
        return null;
    }

}
