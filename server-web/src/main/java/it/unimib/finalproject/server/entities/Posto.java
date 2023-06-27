package it.unimib.finalproject.server.entities;

import java.io.Serializable;

public class Posto implements Serializable, Comparable<Posto> {

    Integer id;
    Integer row;
    Integer column;

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
