package it.unimib.finalproject.database;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Proiezione implements Serializable {

    private int id;
    private Film film;
    private Sala sala;
    private LocalDate data;
    private LocalTime orario;
    private List<Prenotazione> prenotazioni;

    public Proiezione(int id, Film film, Sala sala, LocalDate data, LocalTime orario) {
        this.id = id;
        this.film = film;
        this.sala = sala;
        this.data = data;
        this.orario = orario;
        prenotazioni = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getOrario() {
        return orario;
    }

    public void setOrario(String orario) {
        this.orario = orario;
    }

    public List<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    public void setPrenotazioni(List<Prenotazione> prenotazioni) {
        this.prenotazioni = prenotazioni;
    }

    @Override
    public String toString() {
        return "Proiezione{" +
                "\nid=" + id +
                "\n, film=" + film.toString() +
                "\n, sala=" + sala.toString() +
                "\n, data='" + data + '\'' +
                "\n, orario='" + orario + '\'' +
                "\n, prenotazioni=" + prenotazioni.toString() +
                '}';
    }

}
