package it.unimib.finalproject.server.entities;

import java.util.Objects;

public class Posto implements Comparable<Posto>, IEntity {
	
	Integer id;
    Integer row;
    Integer column;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    @Override
    public boolean notNullAttributes() {
        return row != null &&
                column != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Posto)) return false;
        Posto posto = (Posto) o;
        return Objects.equals(getRow(), posto.getRow()) && Objects.equals(getColumn(), posto.getColumn());
    }

    @Override
    public int compareTo(Posto o) {
        return id.compareTo(o.getId());
    }

    @Override
    public String toString() {
        return "{\"id\": " + id +
                ", \"row\": " + row +
                ", \"column\": " + column + "}";
    }

}
