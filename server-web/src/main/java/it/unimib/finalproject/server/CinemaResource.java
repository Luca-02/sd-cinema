package it.unimib.finalproject.server;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.io.IOException;

@Path("api")
public class CinemaResource {

    /**
     * Porta di connessione per il database
     */
    public static final int PORT = 3030;

    /* - */

    @Path("/film")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFilm() {
        String request = "HSUBGETALL film:";
        ClientChannel c = null;
        try {
            c = new ClientChannel("localhost", PORT, request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert c != null;
        c.start();
        return Response.status(Response.Status.ACCEPTED).build();
    }

}
