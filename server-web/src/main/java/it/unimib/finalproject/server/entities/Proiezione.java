package it.unimib.finalproject.server.entities;

import it.unimib.finalproject.server.utility.DateTimeFormat;
import java.text.ParseException;
import java.util.*;

public class Proiezione implements Comparable<Proiezione>, IEntity {

	private Integer id;
    private Integer idFilm;
    private Integer idSala;
    private String data;
    private String orario;

    @Override
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

    public boolean overlapProiezione(List<Proiezione> proiezioni, List<Film> filmList) throws ParseException {
        Map<Integer, Film> filmMap = new HashMap<>();
        for (var obj : filmList) {
            filmMap.put(obj.getId(), obj);
        }

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

    @Override
    public boolean notNullAttributes() {
        return  id != null &&
                idFilm != null &&
                idSala != null &&
                data != null &&
                orario != null;
    }

    @Override
    public int compareTo(Proiezione o) {
        return data.compareTo(o.getData());
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
