package com.backoffice.model;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Email
    @Column
    private String email;

    @Column
    @NotNull
    private String nome;

    @Column
    @NotNull
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'", timezone="GMT")
    private Date dtNascimento;

    @ManyToOne
    @NotNull
    private Time time;

    @OneToMany(fetch=FetchType.EAGER)
    private List<Campanha> campanhaList;

    public List<Campanha> getCampanhaList() {
        return campanhaList;
    }

    public void setCampanhaList(List<Campanha> campanhaList) {
        this.campanhaList = campanhaList;
    }

    public Cliente() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(Date dtNascimento) {
        this.dtNascimento = dtNascimento;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Cliente(String email, String nome, Date dtNascimento, Time time) {
        this.email = email;
        this.nome = nome;
        this.dtNascimento = dtNascimento;
        this.time = time;
    }

}
