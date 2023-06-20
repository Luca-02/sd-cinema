package it.unimib.finalproject.server.entities;

import java.io.Serializable;

public class Film implements Serializable {

    private int id;
    private String film;
    private int durataMinuti;

    public Film() {
    }

    public Film(int id, String film, int durataMinuti) {
        this.id = id;
        this.film = film;
        this.durataMinuti = durataMinuti;
    }

    public Film(String film, int durataMinuti) {
        this.film = film;
        this.durataMinuti = durataMinuti;
    }

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

    public int getDurataMinuti() {
        return durataMinuti;
    }

    public void setDurataMinuti(int durataMinuti) {
        this.durataMinuti = durataMinuti;
    }

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", film='" + film + '\'' +
                ", durataMinuti=" + durataMinuti +
                '}';
    }
}
