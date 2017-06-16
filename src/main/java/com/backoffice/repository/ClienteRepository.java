package com.backoffice.repository;

import com.backoffice.model.Cliente;
import org.springframework.data.repository.CrudRepository;

public interface ClienteRepository extends CrudRepository<Cliente, Long> {

    Cliente findClienteByEmail(String email);

}
