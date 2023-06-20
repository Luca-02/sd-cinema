package it.unimib.finalproject.server.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Prenotazione implements Serializable {

    private int id;
    private String data;
    private String orario;
    private List<Posto> posti;

    public Prenotazione() {
    }

    public Prenotazione(int id, String data, String orario) {
        this.id = id;
        this.data = data;
        this.orario = orario;
        this.posti = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<Posto> getPosti() {
        return posti;
    }

    public void setPosti(List<Posto> posti) {
        this.posti = posti;
    }

    @Override
    public String toString() {
        return "Prenotazione{" +
                "id=" + id +
                ", postiPrenotazione=" + posti +
                '}';
    }
}
