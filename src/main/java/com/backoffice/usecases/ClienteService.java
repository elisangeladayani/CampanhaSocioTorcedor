package com.backoffice.usecases;

import com.backoffice.entities.Campanha;
import com.backoffice.entities.Cliente;
import com.backoffice.entities.exception.ClienteJaExisteException;
import com.backoffice.entities.exception.ClienteNaoEncontradoException;
import com.backoffice.entities.exception.TimeCoracaoIncompativelException;
import com.backoffice.gateway.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente createCliente(Cliente cliente) throws ClienteJaExisteException {
        if(clienteRepository.findClienteByEmail(cliente.getEmail()) == null) {
            return clienteRepository.save(cliente);
        } else {
            throw new ClienteJaExisteException(cliente.getEmail());
        }
    }

    public Cliente getClienteByEmail(String email) {
        return clienteRepository.findClienteByEmail(email);
    }

    public Cliente getClienteById(Long id) throws ClienteNaoEncontradoException{
        Cliente cliente = clienteRepository.findOne(id);
        if (cliente == null){
            throw new ClienteNaoEncontradoException(id);
        }
        return cliente;
    }

    public Cliente updateClienteById(long id, Cliente cliente) throws ClienteNaoEncontradoException{
        Cliente c = clienteRepository.findOne(id);
        if (c == null){
            throw new ClienteNaoEncontradoException(id);
        } else {
            cliente.setId(id);
            cliente.setCampanhaList(c.getCampanhaList());
        }
        return clienteRepository.save(cliente);
    }

    public Cliente deleteCliente(Long id) throws ClienteNaoEncontradoException{
        Cliente cliente = clienteRepository.findOne(id);
        try {
            clienteRepository.delete(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ClienteNaoEncontradoException(id);
        }
        return cliente;
    }

    public Cliente addCampanhaToCliente(Campanha campanha, Cliente cliente) throws TimeCoracaoIncompativelException {
        if(campanha.getTime().getId()!=cliente.getTime().getId()){
            throw new TimeCoracaoIncompativelException(campanha.getId(), cliente.getId());
        }
        if(cliente.getCampanhaList()==null) {
            cliente.setCampanhaList(new ArrayList<Campanha>() {{
                add(campanha);
            }});
        } else {
            cliente.getCampanhaList().add(campanha);
        }
        return clienteRepository.save(cliente);
    }

    public Object getAllCliente() {
        return clienteRepository.findAll();
    }
}
