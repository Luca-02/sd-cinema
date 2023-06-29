package it.unimib.finalproject.server.entities;

import it.unimib.finalproject.server.utility.DateTimeFormat;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class Prenotazione implements Comparable<Prenotazione>, IEntity {

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

    public Date formattedData() throws ParseException {
        return DateTimeFormat.dateFormat.parse(getData());
    }

    public Date formattedTime() throws ParseException {
        return DateTimeFormat.timeFormat.parse(getOrario());
    }

    public boolean correctDateTimeFormat() {
        boolean check = true;
        try {
            formattedData();
            formattedTime();
        } catch (ParseException e) {
            check = false;
        }
        return check;
    }

    public boolean notNullAttributes() {
        return  id != null &&
                data != null &&
                orario != null;
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
