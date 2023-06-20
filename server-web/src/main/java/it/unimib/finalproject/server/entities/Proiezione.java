package it.unimib.finalproject.server.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Proiezione implements Serializable {

    private int id;
    private int idFilm;
    private int idSala;
    private String data;
    private String orario;
    private List<Prenotazione> prenotazioni;

    public Proiezione() {
    }

    public Proiezione(int id, int idFilm, int idSala, String data, String orario) {
        this.id = id;
        this.idFilm = idFilm;
        this.idSala = idSala;
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

    public int getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(int idFilm) {
        this.idFilm = idFilm;
    }

    public int getIdSala() {
        return idSala;
    }

    public void setIdSala(int idSala) {
        this.idSala = idSala;
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
                "id=" + id +
                ", idFilm=" + idFilm +
                ", idSala=" + idSala +
                ", data=" + data +
                ", orario=" + orario +
                ", prenotazioni=" + prenotazioni +
                '}';
    }
}
