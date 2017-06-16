package com.backoffice.exception;

public class CampanhaNaoEncontradaException extends RuntimeException {

    private Long id;

    public CampanhaNaoEncontradaException(Long id) {
        super("Campanha com id [" + id + "] não encontrado");
        this.id = id;
    }

    @Override
    public String toString() {
        return "Campanha com id [" + this.id + "] não encontrado";
    }
}
