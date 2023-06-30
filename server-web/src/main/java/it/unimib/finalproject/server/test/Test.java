package it.unimib.finalproject.server.test;

import it.unimib.finalproject.server.entities.Prenotazione;
import it.unimib.finalproject.server.entities.Proiezione;

import java.text.ParseException;
import java.util.ArrayList;

public class Test {

    public static void main(String[] args) throws ParseException {
        try {

            Proiezione proiezione = new Proiezione();
            proiezione.setId(0);
            proiezione.setIdFilm(0);
            proiezione.setIdSala(0);
            proiezione.setData("2020-12-12");
            proiezione.setOrario("20:00");

            Prenotazione prenotazione = new Prenotazione();
            prenotazione.setId(0);
            prenotazione.setIdProiezione(0);
            prenotazione.setData("2020-12-13");
            prenotazione.setOrario("19:00");
            prenotazione.setPosti(new ArrayList<>());

            System.out.println(prenotazione.validDateTime(proiezione));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
