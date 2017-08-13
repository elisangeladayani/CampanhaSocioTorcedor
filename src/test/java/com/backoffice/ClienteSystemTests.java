package com.backoffice;

import com.backoffice.entities.Campanha;
import com.backoffice.entities.Cliente;
import com.backoffice.gateway.repository.CampanhaRepository;
import com.backoffice.gateway.repository.ClienteRepository;
import com.backoffice.gateway.repository.TimeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = CampanhaSocioTorcedorApplication.class)
@AutoConfigureMockMvc
@Transactional
public class ClienteSystemTests {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    CampanhaRepository campanhaRepository;

    @Autowired
    TimeRepository timeRepository;

    @Autowired
    private MockMvc mvc;

    @Test
    @Rollback
    public void shouldCreateCliente() throws Exception {

        Cliente cliente = new Cliente("teste999@teste.com", "Cliente Teste", new Date(), timeRepository.findOne(1002l));

        String json = objectMapper.writeValueAsString(cliente);

        this.mvc.perform(post("/api/cliente/").content(json).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nome", is("Cliente Teste")))
                .andExpect(jsonPath("$.email", is("teste999@teste.com")));
    }

    @Test
    @Rollback
    public void shouldReturnValidCampanhasForCliente() throws Exception {
        Cliente cliente = new Cliente("123teste123@teste.com", "Cliente Teste", new Date(), timeRepository.findOne(1002l));

        clienteRepository.save(cliente);

        campanhaRepository.save(new Campanha("Campanha Time RJ",timeRepository.findOne(1002l), new Date(), CampanhaIntegrationTests.geraDataFuturo()));
        campanhaRepository.save(new Campanha("Campanha Time SP",timeRepository.findOne(1002l), new Date(), CampanhaIntegrationTests.geraDataFuturo()));

        String json = objectMapper.writeValueAsString(cliente);

        this.mvc.perform(post("/api/cliente/").content(json).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.message", is("Cliente com email já cadastrado.")));
       ;
    }

    @Test
    @Rollback
    public void shouldReturnClienteById() throws Exception {
        Cliente cliente = new Cliente("teste998@teste.com", "Cliente Teste", new Date(), timeRepository.findOne(1002l));
        cliente = clienteRepository.save(cliente);

        this.mvc.perform(get(String.format("/api/cliente/%d", cliente.getId()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nome", is("Cliente Teste")))
                .andExpect(jsonPath("$.email", is("teste998@teste.com")));
    }

    @Test
    @Rollback
    public void shouldUpdateCliente() throws Exception {
        Cliente cliente = new Cliente("teste9976@teste.com", "Cliente Teste", new Date(), timeRepository.findOne(1002l));
        cliente = clienteRepository.save(cliente);
        cliente.setNome("Cliente Update Test");

        String json = objectMapper.writeValueAsString(cliente);

        this.mvc.perform(put(String.format("/api/cliente/%d", cliente.getId()))
                .content(json).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nome", is("Cliente Update Test")));
    }

    @Test
    @Rollback
    public void shouldDeleteCliente() throws Exception {
        Cliente cliente = new Cliente("teste9916@teste.com", "Cliente Teste", new Date(), timeRepository.findOne(1002l));
        cliente = clienteRepository.save(cliente);

        this.mvc.perform(delete(String.format("/api/cliente/%d", cliente.getId())).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.email", is("teste9916@teste.com")));
    }

}

