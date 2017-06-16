package com.backoffice;

import com.backoffice.controller.ClienteController;
import com.backoffice.exception.CampanhaNaoEncontradaException;
import com.backoffice.exception.ClienteNaoEncontradoException;
import com.backoffice.exception.TimeCoracaoIncompativelException;
import com.backoffice.model.Campanha;
import com.backoffice.model.CampanhasDisponiveisClienteProjection;
import com.backoffice.model.Cliente;
import com.backoffice.repository.CampanhaRepository;
import com.backoffice.repository.ClienteRepository;
import com.backoffice.repository.TimeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClienteIntegrationTests {

    @Autowired
    ClienteController clienteController;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    CampanhaRepository campanhaRepository;

    @Autowired
    TimeRepository timeRepository;

    @Test
    @Rollback
    public void createClienteTest() throws Exception {
        Cliente cliente = new Cliente("123teste@teste.com", "Cliente Teste", new Date(), timeRepository.findOne(1002l));
        Cliente novoCliente = (Cliente)clienteController.createCliente(cliente);
        assertEquals("123teste@teste.com", novoCliente.getEmail());
    }

    @Test
    @Rollback
    public void createClienteAlreadyExistsThenShowCampanhasListTest() throws Exception {
        campanhaRepository.save(new Campanha("Campanha testes cliente",timeRepository.findOne(1003l), new Date(), CampanhaIntegrationTests.geraDataFuturo()));
        campanhaRepository.save(new Campanha("Campanha 2 testes cliente",timeRepository.findOne(1003l), new Date(), CampanhaIntegrationTests.geraDataFuturo()));
        Cliente cliente = new Cliente("123testeErro123@teste.com", "Cliente Teste", new Date(), timeRepository.findOne(1003l));
        clienteController.createCliente(cliente);
        Cliente newCliente = new Cliente("123testeErro123@teste.com", "Segundo Cliente Teste", new Date(), timeRepository.findOne(1001l));
        CampanhasDisponiveisClienteProjection response = (CampanhasDisponiveisClienteProjection)clienteController.createCliente(newCliente);
        ArrayList<Campanha> campanhas = new ArrayList<>();
        response.getCampanhasDisponiveis().iterator().forEachRemaining(campanhas::add);
        assertTrue(campanhas.size()==2);
    }

    @Test
    @Rollback
    public void addCampanhaToNewClienteTest() throws Exception {
        Cliente newCliente = new Cliente("testeErro@teste.com", "Segundo Cliente Teste", new Date(), timeRepository.findOne(1002l));
        Campanha campanha = campanhaRepository.save(new Campanha("Campanha testes cliente",timeRepository.findOne(1002l), new Date(), CampanhaIntegrationTests.geraDataFuturo()));
        clienteController.createCliente(newCliente);
        Cliente cliente = clienteController.addCampanhaToCliente(campanha.getId(), newCliente.getId());
        assertTrue(cliente.getCampanhaList().get(0).getId()==campanha.getId());
    }

    @Test
    @Rollback
    public void getClienteByIdTest() {
        Cliente c = new Cliente("cliente1@test.com","Teste Cliente", new Date(), timeRepository.findOne(1002l));
        c = clienteRepository.save(c);
        Cliente result = clienteController.getClienteById(c.getId());
        assertEquals(c.getNome(), result.getNome());
    }

    @Test(expected = ClienteNaoEncontradoException.class)
    @Rollback
    public void getClienteByIdFailNaoEncontradoTest(){
        clienteController.getClienteById(-999l);
    }

    @Test
    @Rollback
    public void updateClienteTest() throws IllegalArgumentException {
        Cliente c = new Cliente("cliente928@test.com","Teste Cliente", new Date(), timeRepository.findOne(1002l));
        c = clienteRepository.save(c);
        c.setEmail("clienteUpdate@teste.com");
        clienteController.updateClienteById(c, c.getId());
        assertEquals("clienteUpdate@teste.com", clienteController.getClienteById(c.getId()).getEmail());
    }


    @Test(expected = ClienteNaoEncontradoException.class)
    @Rollback
    public void updateClienteFailNaoEncontradoTest() throws IllegalArgumentException {
        clienteController.updateClienteById(new Cliente(), -999l);
    }

    @Test(expected = ClienteNaoEncontradoException.class)
    @Rollback
    public void deleteCampanhaTest() throws IllegalArgumentException {
        Cliente c = new Cliente("cliente928@test.com","Teste Cliente", new Date(), timeRepository.findOne(1002l));
        c = clienteRepository.save(c);
        c = clienteController.deleteCliente(c.getId());
        clienteController.getClienteById(c.getId());
    }

    @Test(expected = ClienteNaoEncontradoException.class)
    @Rollback
    public void deleteClienteFailNaoEncontradoTest() throws ParseException {
        clienteController.deleteCliente(-1l);
    }

    @Test(expected = ClienteNaoEncontradoException.class)
    @Rollback
    public void addCampanhaToClienteFailClienteNaoEncontradoTest() throws Exception {
        Campanha c = new Campanha("Campanha a ser adicionada", timeRepository.findOne(1002l), new Date(), CampanhaIntegrationTests.geraDataFuturo());
        campanhaRepository.save(c);
        long idCliente = -1l;
        clienteController.addCampanhaToCliente(c.getId(),idCliente);
    }

    @Test(expected = CampanhaNaoEncontradaException.class)
    @Rollback
    public void addCampanhaToClienteFailCampanhaNaoEncontradaTest() throws Exception {
        Cliente cl = new Cliente("cliente9286@test.com","Teste Cliente", new Date(), timeRepository.findOne(1002l));
        cl = clienteRepository.save(cl);
        long idCampanha = -1l;
        clienteController.addCampanhaToCliente(idCampanha,cl.getId());
    }

    @Test
    @Rollback
    public void addCampanhaToClienteTest() throws Exception {
        Cliente cl = new Cliente("cliente5286@test.com","Teste Cliente", new Date(), timeRepository.findOne(1002l));
        cl = clienteRepository.save(cl);
        Campanha ca = new Campanha("Campanha adicionada", timeRepository.findOne(1002l), new Date(), CampanhaIntegrationTests.geraDataFuturo());
        campanhaRepository.save(ca);
        cl = clienteController.addCampanhaToCliente(ca.getId(),cl.getId());
        assertEquals("cliente5286@test.com",cl.getEmail());
        assertTrue(cl.getCampanhaList().size()==1);
        assertEquals("Campanha adicionada",cl.getCampanhaList().get(0).getNome());
    }

    @Test(expected = TimeCoracaoIncompativelException.class)
    @Rollback
    public void addCampanhaToClienteFailTimeTest() throws Exception {
        Cliente cl = new Cliente("cliente5086@test.com","Teste Cliente", new Date(), timeRepository.findOne(1002l));
        cl = clienteRepository.save(cl);
        Campanha ca = new Campanha("Campanha adicionada", timeRepository.findOne(1001l), new Date(), CampanhaIntegrationTests.geraDataFuturo());
        campanhaRepository.save(ca);
        clienteController.addCampanhaToCliente(ca.getId(),cl.getId());
    }

}
