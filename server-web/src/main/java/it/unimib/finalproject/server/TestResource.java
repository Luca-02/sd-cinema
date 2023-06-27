package it.unimib.finalproject.server;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unimib.finalproject.server.entities.*;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Path("test/api")
public class TestResource {

    /**
     * Porta di ascolto.
     */
    public static final int PORT = 3030;

    /* - */

    private static ConcurrentHashMap<Integer, Film> filmMapTest = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Integer, Sala> saleMapTest = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Integer, Proiezione> proiezioniMapTest = new ConcurrentHashMap<>();

    static {
        Film f = new Film();
        f.setId(0);
        f.setFilm("film1");
        f.setDurataMinuti(120);
        filmMapTest.put(f.getId(), f);

        f = new Film();
        f.setId(1);
        f.setFilm("film2");
        f.setDurataMinuti(60);
        filmMapTest.put(f.getId(), f);

        Sala s = new Sala();
        s.setId(0);
        s.setNome("a");
        s.setRow(2);
        s.setColumns(3);
        saleMapTest.put(s.getId(), s);

        s = new Sala();
        s.setId(1);
        s.setNome("b");
        s.setRow(2);
        s.setColumns(3);
        saleMapTest.put(s.getId(), s);

        Proiezione pro = new Proiezione();
        pro.setId(0);
        pro.setIdFilm(filmMapTest.get(0).getId());
        pro.setIdSala(saleMapTest.get(0).getId());
        pro.setData("2005-05-12");
        pro.setOrario("10:15");
//        pro.setPrenotazioni(new ArrayList<>());
        proiezioniMapTest.put(pro.getId(), pro);

        Prenotazione pre = new Prenotazione();
        pre.setId(0);
        pre.setData("2005-05-11");
        pre.setOrario("20:30");
//        pre.setPosti(new ArrayList<>());
//        proiezioniMapTest.get(0).getPrenotazioni().add(pre);

        pre = new Prenotazione();
        pre.setId(1);
        pre.setData("2005-04-11");
        pre.setOrario("20:00");
//        pre.setPosti(new ArrayList<>());
//        proiezioniMapTest.get(0).getPrenotazioni().add(pre);

        List<Posto> posti = new ArrayList<>();
        Posto posto = new Posto();
        posto.setId(0);
        posto.setRow(2);
        posto.setColumn(5);
        posti.add(posto);
        posto = new Posto();
        posto.setId(1);
        posto.setRow(3);
        posto.setColumn(3);
        posti.add(posto);
//        pre.setPosti(posti);
    }

    @Path("/film")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Film> getFilm() {
        return new ArrayList<>(filmMapTest.values());
    }

    @Path("/film")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addFilm(String body) {
        try {
            var mapper = new ObjectMapper();
            var obj = mapper.readValue(body, Film.class);

            List<Film> entryList =
                    new ArrayList<>(filmMapTest.values());

            int id =
                    entryList.size() > 0 ?
                            entryList.get(entryList.size() - 1).getId() + 1 : 0;

            obj.setId(id);
            filmMapTest.put(obj.getId(), obj);

            var uri = new URI("test/api/film/" + obj.getId());
            return Response.created(uri).build();
        } catch (JsonParseException | JsonMappingException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @Path("/film/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFilm(@PathParam("id") int idFilm) {
        var kb = filmMapTest.get(idFilm);
        if (kb != null)
            return Response.ok(kb).build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();
    }

    /* - */

    @Path("/sala")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sala> getSala() {
        return new ArrayList<>(saleMapTest.values());
    }

    @Path("/sala")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSala(String body) {
        try {
            var mapper = new ObjectMapper();
            var obj = mapper.readValue(body, Sala.class);

            List<Sala> entryList =
                    new ArrayList<>(saleMapTest.values());

            int id =
                    entryList.size() > 0 ?
                            entryList.get(entryList.size() - 1).getId() + 1 : 0;

            obj.setId(id);
            saleMapTest.put(obj.getId(), obj);

            var uri = new URI("test/api/sala/" + obj.getId());
            return Response.created(uri).build();
        } catch (JsonParseException | JsonMappingException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @Path("/sala/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSala(@PathParam("id") int idSala) {
        var kb = saleMapTest.get(idSala);
        if (kb != null)
            return Response.ok(kb).build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();
    }

    /* - */

    @Path("/proiezione")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Proiezione> getProiezione() {
        return new ArrayList<>(proiezioniMapTest.values());
    }

    @Path("/proiezione")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProiezione(String body) {
        try {
            var mapper = new ObjectMapper();
            var obj = mapper.readValue(body, Proiezione.class);

            List<Proiezione> entryList =
                    new ArrayList<>(proiezioniMapTest.values());

            int id =
                    entryList.size() > 0 ?
                            entryList.get(entryList.size() - 1).getId() + 1 : 0;

            obj.setId(id);
//            obj.setPrenotazioni(new ArrayList<>());
            proiezioniMapTest.put(obj.getId(), obj);

            var uri = new URI("test/api/proiezione/" + obj.getId());
            return Response.created(uri).build();
        } catch (JsonParseException | JsonMappingException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @Path("/proiezione/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProiezione(@PathParam("id") int idProiezione) {
        var kb = proiezioniMapTest.get(idProiezione);
        if (kb != null)
            return Response.ok(kb).build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();
    }

    /*

    @Path("/proiezione/{id}/prenotazione")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Prenotazione> getPrenotazione(@PathParam("id") int idProiezione) {
        return new ArrayList<>(proiezioniMapTest.get(idProiezione).getPrenotazioni());
    }

    @Path("/proiezione/{id}/prenotazione")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPrenotazione(@PathParam("id") int idProiezione,
                                    String body) {
        try {
            var mapper = new ObjectMapper();
            var obj = mapper.readValue(body, Prenotazione.class);

            var proiezione = proiezioniMapTest.get(idProiezione);

            List<Prenotazione> entryList = proiezione.getPrenotazioni();

            int id =
                    entryList.size() > 0 ?
                            entryList.get(entryList.size() - 1).getId() + 1 : 0;

            obj.setId(id);
            proiezione.getPrenotazioni().add(obj);

            var uri = new URI("test/api/proiezione/" + idProiezione + "/prenotazione/" + obj.getId());
            return Response.created(uri).build();
        } catch (JsonParseException | JsonMappingException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @Path("/proiezione/{id}/prenotazione/{id2}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrenotazione(@PathParam("id") int idProiezione,
                                    @PathParam("id2") int idPrenotazione) {
        var proiezione = proiezioniMapTest.get(idProiezione);
        if (proiezione != null) {
            for (Prenotazione prenotazione : proiezione.getPrenotazioni()) {
                if (prenotazione.getId() == idPrenotazione)
                    return Response.ok(prenotazione).build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Path("/proiezione/{id}/prenotazione/{id2}")
    @DELETE
    public Response deletePrenotazione(@PathParam("id") int idProiezione,
                                       @PathParam("id2") int idPrenotazione) {
        var proiezione = proiezioniMapTest.get(idProiezione);
        if (proiezione != null) {
            if (proiezione.getPrenotazioni().removeIf(obj -> obj.getId() == idPrenotazione))
                return Response.noContent().build();
            else
                return Response.status(Response.Status.NOT_FOUND).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Path("/proiezione/{id}/prenotazione/{id2}/posto/{id3}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deletePostoPrenotazione(@PathParam("id") int idProiezione,
                                            @PathParam("id2") int idPrenotazione,
                                            @PathParam("id3") int idPosto) {
        var proiezione = proiezioniMapTest.get(idProiezione);
        if (proiezione != null) {
            Prenotazione prenotazione = null;
            for (Prenotazione pre : proiezione.getPrenotazioni()) {
                if (pre.getId() == idPrenotazione)
                    prenotazione = pre;
            }

            if (prenotazione != null) {
                if (prenotazione.getPosti().removeIf(obj -> obj.getId() == idPosto))
                    return Response.noContent().build();
                else
                    return Response.status(Response.Status.NOT_FOUND).build();
            }
            else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

     */

}
