package it.unimib.finalproject.server.entities;


public class Sala implements Comparable<Sala>, IEntity {

	private Integer id;
    private String nome;
    private Integer row;
    private Integer columns;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
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

    public boolean notNullAttributes() {
        return  id != null &&
                nome != null &&
                row != null &&
                columns != null;
    }

    @Override
    public String toString() {
        return "{\"id\": " + id +
                ", \"nome\": \"" + nome + "\"" +
                ", \"row\": " + row +
                ", \"columns\": " + columns + "}";
    }

}
