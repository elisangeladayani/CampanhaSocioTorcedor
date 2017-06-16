package com.backoffice.controller;

import com.backoffice.model.Campanha;
import com.backoffice.model.CampanhasDisponiveisClienteProjection;
import com.backoffice.model.Cliente;
import com.backoffice.exception.*;
import com.backoffice.service.CampanhaService;
import com.backoffice.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.stream.Collectors;

@RestController
public class ClienteController {

    @Autowired
    ClienteService clienteService;

    @Autowired
    CampanhaService campanhaService;

    @PostMapping(value = "/api/cliente", consumes = "application/json;charset=UTF-8")
    public Object createCliente(@RequestBody @Valid Cliente cliente) {
        try {
            return clienteService.createCliente(cliente);
        } catch (ClienteJaExisteException e){
            Cliente clienteExistente = clienteService.getClienteByEmail(cliente.getEmail());
            return montarProjectionCampanhasCliente(clienteExistente, campanhaService.getValidCampanhaByTime(clienteExistente.getTime().getId()));
        }
    }

    private CampanhasDisponiveisClienteProjection montarProjectionCampanhasCliente(Cliente cliente, Iterable<Campanha> campanhaList){
        if(cliente.getCampanhaList() != null) {
            ArrayList<Campanha> campanhas = new ArrayList<>();
            campanhaList.iterator().forEachRemaining(campanhas::add);
            ArrayList<Campanha> campanhasFiltradas = campanhas.stream().filter(campanha -> !cliente.getCampanhaList().contains(campanha)).collect(Collectors.toCollection(ArrayList::new));
            return new CampanhasDisponiveisClienteProjection(campanhasFiltradas, cliente);
        }
        return new CampanhasDisponiveisClienteProjection(campanhaList, cliente);
    }

    @GetMapping(value = "/api/cliente/{id}")
    public Cliente getClienteById(@PathVariable("id") Long id) throws ClienteNaoEncontradoException {
        return clienteService.getClienteById(id);
    }

    @GetMapping(value = "/api/cliente/all")
    public Object getAllClientes() {
        return clienteService.getAllCliente();
    }

    @PutMapping(value = "/api/cliente/{id}")
    public Cliente updateClienteById(@RequestBody @Valid Cliente cliente, @PathVariable("id") Long id)
            throws ClienteNaoEncontradoException{
        return clienteService.updateClienteById(id,cliente);
    }

    @DeleteMapping("/api/cliente/{id}")
    public Cliente deleteCliente(@PathVariable("id") Long id) throws ClienteNaoEncontradoException {
        return clienteService.deleteCliente(id);
    }

    @PostMapping(value = "/api/cliente",params = {"idCliente", "idCampanha"})
    public Cliente addCampanhaToCliente(@RequestParam("idCampanha") long idCampanha, @RequestParam("idCliente") long idCliente)
            throws ClienteNaoEncontradoException, CampanhaNaoEncontradaException, TimeCoracaoIncompativelException, CampanhaExpiradaException {
        Cliente cliente = clienteService.getClienteById(idCliente);
        Campanha campanha = campanhaService.getValidCampanhaById(idCampanha);
        return clienteService.addCampanhaToCliente(campanha, cliente);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleException(ClienteNaoEncontradoException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleException(TimeCoracaoIncompativelException ex) {
        return ex.getMessage();
    }



}
