package it.unimib.finalproject.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Prenotazione implements Serializable {

    private int id;
    private String data;
    private String orario;
    private List<Posto> posti;

    public Prenotazione(int id, int idPrenotazione, List<Posto> postiPrenotazione) {
        this.id = id;
        posti = new ArrayList<>(postiPrenotazione);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Posto> getPostiPrenotazione() {
        return posti;
    }

    public void setPostiPrenotazione(List<Posto> postiPrenotazione) {
        this.posti = postiPrenotazione;
    }

    public void deletePosto (int idPosto) {

    }

    @Override
    public String toString() {
        return "Prenotazione{" +
                "id=" + id +
                ", postiPrenotazione=" + posti +
                '}';
    }
}
