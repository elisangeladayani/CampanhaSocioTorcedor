package com.backoffice.entities.exception;

public class TimeCoracaoIncompativelException extends Exception {

    private Long idCampanha;
    private Long idCliente;

    public TimeCoracaoIncompativelException(Long idCampanha, Long idCliente) {
        super("Cliente com id [" + idCliente + "] não suporta campanha com id ["+ idCampanha + "] pois times são diferentes");
        this.idCliente = idCliente;
        this.idCampanha = idCampanha;
    }

    @Override
    public String toString() {
        return "Cliente com id [" + idCliente + "] não suporta campanha com id ["+ idCampanha + "] pois times são diferentes";
    }
}
