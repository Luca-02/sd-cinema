package it.unimib.finalproject.server.entities;

import it.unimib.finalproject.server.utility.DateTimeFormat;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class Prenotazione implements Comparable<Prenotazione>, IEntity {

	private Integer id;
    private Integer idProiezione;
    private String data;
    private String orario;
    private List<Posto> posti;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdProiezione() {
        return idProiezione;
    }

    public void setIdProiezione(Integer idProiezione) {
        this.idProiezione = idProiezione;
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

    public void initCurrentDateTime() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimeFormat.dateFormatString);
        data = currentDate.format(formatter);

        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern(DateTimeFormat.timeFormatString);
        orario = currentTime.format(formatter1);
    }

    public boolean validDateTime(Proiezione proiezione)
            throws ParseException {
        if (formattedData().before(proiezione.formattedData())) {
            return true;
        }
        else {
            if (formattedData().equals(proiezione.formattedData()))
                return formattedTime().before(proiezione.formattedTime());
            else
                return false;
        }
    }

    @Override
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
                ", \"idProiezione\": " + idProiezione +
                ", \"data\": \"" + data + "\"" +
                ", \"orario\": \"" + orario + "\"}";
    }
}
