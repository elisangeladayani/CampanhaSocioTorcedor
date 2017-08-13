package com.backoffice.gateway.repository;

import com.backoffice.entities.Cliente;
import org.springframework.data.repository.CrudRepository;

public interface ClienteRepository extends CrudRepository<Cliente, Long> {

    Cliente findClienteByEmail(String email);

}
