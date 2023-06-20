package it.unimib.finalproject.server.entities;

import java.io.Serializable;

public class Sala implements Serializable {

    private int id;
    private String nome;
    private int row;
    private int columns;

    public Sala() {
    }

    public Sala(int id, String nome, int row, int columns) {
        this.id = id;
        this.nome = nome;
        this.row = row;
        this.columns = columns;
    }

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

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    @Override
    public String toString() {
        return "Sala{" +
                "id=" + id +
                ", row=" + row +
                ", columns=" + columns +
                '}';
    }

}
