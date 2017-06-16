package com.backoffice.exception;

public class ClienteNaoEncontradoException extends RuntimeException {

    private Long id;

    public ClienteNaoEncontradoException(Long id) {
        super("Cliente com id [" + id + "] não encontrado");
        this.id = id;
    }

    @Override
    public String toString() {
        return "Cliente com id [" + id + "] não encontrado";
    }
}
