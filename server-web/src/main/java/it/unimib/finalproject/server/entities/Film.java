package it.unimib.finalproject.server.entities;

import java.io.Serializable;

public class Film implements Serializable, Comparable<Film> {

    private Integer id;
    private String film;
    private Integer durataMinuti;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilm() {
        return film;
    }

    public void setFilm(String film) {
        this.film = film;
    }

    public Integer getDurataMinuti() {
        return durataMinuti;
    }

    public void setDurataMinuti(Integer durataMinuti) {
        this.durataMinuti = durataMinuti;
    }

    public boolean notNullAttributes() {
        return  id != null &&
                film != null &&
                durataMinuti != null;
    }

    @Override
    public int compareTo(Film o) {
        return id.compareTo(o.getId());
    }

    @Override
    public String toString() {
        return "{\"id\": " + id +
                ", \"film\": \"" + film + "\"" +
                ", \"durataMinuti\": " + durataMinuti + "}";
    }

}
