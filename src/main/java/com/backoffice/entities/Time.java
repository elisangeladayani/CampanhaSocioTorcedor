package com.backoffice.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Time {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column
    @NotNull
    private String nome;

    public Time(long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Time() {
    }
}
