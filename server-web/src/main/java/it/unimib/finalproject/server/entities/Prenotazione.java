package it.unimib.finalproject.server.entities;

import java.io.Serializable;
import java.util.List;

public class Prenotazione implements Serializable, Comparable<Prenotazione> {

    private Integer id;
    private String data;
    private String orario;
    private List<Posto> posti;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId(Integer id) {
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
    public int compareTo(Prenotazione o) {
        return id.compareTo(o.getId());
    }

    @Override
    public String toString() {
        return "{\"id\": " + id +
                ", \"data\": \"" + data + "\"" +
                ", \"orario\": \"" + orario + "\"}";
    }
}
