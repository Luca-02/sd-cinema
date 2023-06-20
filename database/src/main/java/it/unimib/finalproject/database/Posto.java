package it.unimib.finalproject.database;

import java.io.Serializable;

public class Posto implements Serializable {

    int id;
    int row;
    int column;

    public Posto(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public String toString() {
        return "Posto{" +
                "row=" + row +
                ", column=" + column +
                '}';
    }

}
