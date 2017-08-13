package com.backoffice;

import com.backoffice.entities.Campanha;
import com.backoffice.gateway.repository.CampanhaRepository;
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
public class CampanhaSystemTests {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CampanhaRepository campanhaRepository;

    @Autowired
    private MockMvc mvc;

    @Autowired
    TimeRepository timeRepository;

    @Test
    public void shouldReturnNotFound() throws Exception {
        this.mvc.perform(get("/api/campanha/1").accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Rollback
    public void shouldCreateCampanha() throws Exception {

        Campanha campanha = new Campanha();
        campanha.setNome("Nova campanha");
        campanha.setTime(timeRepository.findOne(1001l));
        campanha.setDtInicio(new Date());
        campanha.setDtFim(new Date());

        String campanhaJson = objectMapper.writeValueAsString(campanha);

        this.mvc.perform(post("/api/campanha/").content(campanhaJson).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nome", is("Nova campanha")));
    }

    @Test
    @Rollback
    public void shouldReturnCampanha() throws Exception {

        Campanha campanha = new Campanha();
        campanha.setNome("Nova campanha");
        campanha.setTime(timeRepository.findOne(1001l));
        campanha.setDtInicio(new Date());
        campanha.setDtFim(CampanhaIntegrationTests.geraDataFuturo());
        Campanha campanhaPersisted = campanhaRepository.save(campanha);

        this.mvc.perform(get(String.format("/api/campanha/%d", campanhaPersisted.getId())).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Nova campanha")));
    }


    @Test
    @Rollback
    public void shouldUpdateCampanha() throws Exception {

        Campanha campanha = new Campanha();
        campanha.setNome("Nova campanha");
        campanha.setTime(timeRepository.findOne(1001l));
        campanha.setDtInicio(new Date());
        campanha.setDtFim(CampanhaIntegrationTests.geraDataFuturo());
        Campanha campanhaPersisted = campanhaRepository.save(campanha);
        campanhaPersisted.setNome("Campanha atualizada");

        String campanhaJson = objectMapper.writeValueAsString(campanhaPersisted);

        this.mvc.perform(put(String.format("/api/campanha/%d", campanhaPersisted.getId()))
                .content(campanhaJson).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nome", is("Campanha atualizada")));
    }

    @Test
    @Rollback
    public void shouldDeleteCampanha() throws Exception {

        Campanha campanha = new Campanha();
        campanha.setNome("Nova campanha");
        campanha.setTime(timeRepository.findOne(1001l));
        campanha.setDtInicio(new Date());
        campanha.setDtFim(CampanhaIntegrationTests.geraDataFuturo());
        Campanha campanhaPersisted = campanhaRepository.save(campanha);

        this.mvc.perform(delete(String.format("/api/campanha/%d", campanhaPersisted.getId())).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nome", is("Nova campanha")));
    }

}
