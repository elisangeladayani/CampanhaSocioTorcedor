package com.backoffice.entities;

public class CampanhasDisponiveisClienteProjection {
    private final String message = "Cliente com email já cadastrado.";

    private Cliente cliente;

    private Iterable<Campanha> campanhasDisponiveis;

    public CampanhasDisponiveisClienteProjection(Iterable<Campanha> campanhasDisponiveis, Cliente cliente) {
        this.campanhasDisponiveis = campanhasDisponiveis;
        this.cliente = cliente;
    }

    public Iterable<Campanha> getCampanhasDisponiveis() {
        return campanhasDisponiveis;
    }

    public void setCampanhasDisponiveis(Iterable<Campanha> campanhasDisponiveis) {
        this.campanhasDisponiveis = campanhasDisponiveis;
    }

    public CampanhasDisponiveisClienteProjection() {
    }

    public String getMessage() {
        return message;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
