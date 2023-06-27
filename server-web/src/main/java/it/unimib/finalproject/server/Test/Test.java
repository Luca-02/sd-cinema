package it.unimib.finalproject.server.Test;

import it.unimib.finalproject.server.entities.Film;
import it.unimib.finalproject.server.entities.Proiezione;

import java.text.ParseException;
import java.util.*;

public class Test {
    public static final String excapeDelimiter = "\r\n!#!\r\n";
    public static final String keyValueDelimiter = "!#!";

    public static void main(String[] args) throws ParseException {
        HashMap<Integer, Film> films = new HashMap<>();
        Film film = new Film();
        film.setId(0);
        film.setFilm("test1");
        film.setDurataMinuti(31);
        films.put(0, film);

        Film film1 = new Film();
        film1.setId(1);
        film1.setFilm("test2");
        film1.setDurataMinuti(26);
        films.put(1, film1);

        Proiezione pNew = new Proiezione();
        pNew.setId(0);
        pNew.setIdFilm(0);
        pNew.setIdSala(9);
        pNew.setData("2020-10-10");
        pNew.setOrario("21:00");

        Proiezione p = new Proiezione();
        p.setId(1);
        p.setIdFilm(1);
        p.setIdSala(9);
        p.setData("2020-10-10");
        p.setOrario("20:00");

        List<Proiezione> proiezioni = new ArrayList<>();
        proiezioni.add(p);

        System.out.println(pNew.formattedTime());
        System.out.println(p.formattedTime());

        System.out.println(pNew.formattedTime().compareTo(p.formattedTime()));

        System.out.println("---");

        System.out.println(pNew.proiezioneSovrapposta(proiezioni, films));

    }
}
