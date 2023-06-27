package it.unimib.finalproject.server.entities;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Proiezione implements Serializable, Comparable<Proiezione> {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");

    private Integer id;
    private Integer idFilm;
    private Integer idSala;
    private String data;
    private String orario;
    private List<Prenotazione> prenotazioni;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(Integer idFilm) {
        this.idFilm = idFilm;
    }

    public Integer getIdSala() {
        return idSala;
    }

    public void setIdSala(Integer idSala) {
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

    public Date formattedData() throws ParseException {
        return dateFormat.parse(getData());
    }

    public Date formattedTime() throws ParseException {
        return timeFormat.parse(getOrario());
    }

    public boolean proiezioneSovrapposta(List<Proiezione> proiezioni, Map<Integer, Film> filmMap) throws ParseException {
        boolean check = false;
        for (var obj : proiezioni) {
            if (!Objects.equals(getId(), obj.getId()) &&
                    Objects.equals(getIdSala(), obj.getIdSala()) &&
                    formattedData().compareTo(obj.formattedData()) == 0) {

                Calendar calendar = Calendar.getInstance();

                if (formattedTime().compareTo(obj.formattedTime()) > 0) {
                    Film film = filmMap.get(obj.getIdFilm());

                    calendar.setTime(obj.formattedTime());
                    calendar.add(Calendar.MINUTE, film.getDurataMinuti());
                    Date editTime = calendar.getTime();

                    return !(formattedTime().compareTo(editTime) > 0);
                }
                else if (formattedTime().compareTo(obj.formattedTime()) < 0) {
                    Film film = filmMap.get(getIdFilm());

                    calendar.setTime(formattedTime());
                    calendar.add(Calendar.MINUTE, film.getDurataMinuti());
                    Date editTime = calendar.getTime();

                    return !(editTime.compareTo(obj.formattedTime()) < 0);
                }
                else {
                    return true;
                }
            }
        }
        return check;
    }

    public boolean notNullAttributes() {
        return  id != null &&
                idFilm != null &&
                idSala != null &&
                data != null &&
                orario != null;
    }

    @Override
    public int compareTo(Proiezione o) {
        return id.compareTo(o.getId());
    }

    @Override
    public String toString() {
        return "{\"id\": " + id +
                ", \"idFilm\": " + idFilm +
                ", \"idSala\": " + idSala +
                ", \"data\": \"" + data + "\"" +
                ", \"orario\": \"" + orario + "\"}";
    }

}
