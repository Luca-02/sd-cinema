package it.unimib.finalproject.server.entities;

public class Film implements Comparable<Film>, IEntity{

	private Integer id;
	private String film;
    private Integer durataMinuti;

    @Override
	public Integer getId() {
		return this.id;
	}
	
    public void setId(Integer id) {
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

    @Override
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
