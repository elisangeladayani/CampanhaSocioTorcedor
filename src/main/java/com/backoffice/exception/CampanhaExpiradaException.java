package com.backoffice.exception;

public class CampanhaExpiradaException extends RuntimeException {
    private Long id;

    public CampanhaExpiradaException(Long id) {
        super("Campanha com id [" + id + "] expirada");
        this.id = id;
    }

    @Override
    public String toString() {
        return "Campanha com id [" + this.id + "] expirada";
    }
}
