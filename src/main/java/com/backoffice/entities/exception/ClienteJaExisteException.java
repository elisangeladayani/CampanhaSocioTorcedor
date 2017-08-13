package com.backoffice.entities.exception;

public class ClienteJaExisteException extends RuntimeException {

    private String email;

    public ClienteJaExisteException(String email) {
        super("Cliemte com email [" + email + "] já existe.");
        this.email = email;
    }

    @Override
    public String toString() {
        return "Cliente com email [" + this.email + "] já existe.";
    }
}
