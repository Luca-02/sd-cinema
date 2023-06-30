package it.unimib.finalproject.server.entities;


public class Sala implements Comparable<Sala>, IEntity {

	private Integer id;
    private String nome;
    private Integer rows;
    private Integer columns;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getColumns() {
        return columns;
    }

    public void setColumns(Integer columns) {
        this.columns = columns;
    }

    @Override
    public int compareTo(Sala o) {
        return id.compareTo(o.getId());
    }

    @Override
    public boolean notNullAttributes() {
        return  id != null &&
                nome != null &&
                rows != null &&
                columns != null;
    }

    @Override
    public String toString() {
        return "{\"id\": " + id +
                ", \"nome\": \"" + nome + "\"" +
                ", \"rows\": " + rows +
                ", \"columns\": " + columns + "}";
    }

}
