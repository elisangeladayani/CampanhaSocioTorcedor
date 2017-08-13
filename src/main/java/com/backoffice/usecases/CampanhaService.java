package com.backoffice.usecases;

import com.backoffice.entities.exception.CampanhaExpiradaException;
import com.backoffice.entities.exception.CampanhaNaoEncontradaException;
import com.backoffice.entities.Campanha;
import com.backoffice.gateway.repository.CampanhaRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CampanhaService {

    @Autowired
    private CampanhaRepository campanhaRepository;

    public Campanha getValidCampanhaById(Long id) throws CampanhaExpiradaException, CampanhaNaoEncontradaException{
        Campanha campanha = campanhaRepository.findOne(id);
        if(campanha==null){
            throw new CampanhaNaoEncontradaException(id);
        }
        if(campanha.getDtFim().before(new Date())){
            throw new CampanhaExpiradaException(id);
        }
        return campanha;
    }

    public Campanha createCampanha(Campanha campanha) throws IllegalArgumentException {
        if(campanha.getDtInicio().after(campanha.getDtFim())){
            throw new IllegalArgumentException(new String("dtInicio, dtFim"));
        }

        ArrayList<Campanha> campanhasAtivas = buscaTodasCampanhasAtivasOrdenadas(campanha.getDtInicio());

        adicionaDiaEmCampanhasNoPeriodoConflito(campanhasAtivas, campanha.getDtFim());

        eliminaConflitoDatasEmTodasCampanhasEPersiste(campanha.getDtFim(), campanhasAtivas);

        return campanhaRepository.save(campanha);
    }

    private ArrayList<Campanha> buscaTodasCampanhasAtivasOrdenadas(Date dt){
        dt = converteDataParaMeiaNoite(dt);
        Iterable<Campanha> allCampanhaInVigencia = campanhaRepository.findAllCampanhaInVigencia(dt);
        ArrayList<Campanha> campanhasAtivas = new ArrayList<>();
        allCampanhaInVigencia.iterator().forEachRemaining(campanhasAtivas::add);
        campanhasAtivas.sort(Comparator.comparing(Campanha::getDtFim));
        return campanhasAtivas;
    }

    private Date converteDataParaMeiaNoite(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private void adicionaDiaEmCampanhasNoPeriodoConflito(ArrayList<Campanha> campanhasAtivas, Date dt){
        campanhasAtivas.forEach(c -> {
            if(!(c.getDtFim().compareTo(dt)>0)) {
                adicionaDiasCampanha(c, 1);
            }
        });
    }

    private void eliminaConflitoDatasEmTodasCampanhasEPersiste(Date dtFim, ArrayList<Campanha> campanhasAtivas) {
        Set<Date> datas = new HashSet<>();
        datas.add(dtFim);
        campanhasAtivas.forEach(c -> {
            boolean isAlterado = false;
            while(!datas.add(c.getDtFim())){
                adicionaDiasCampanha(c, 1);
                isAlterado = true;
            }
            if(isAlterado){
                campanhaRepository.save(c);
            }
        });
    }

    private void adicionaDiasCampanha(Campanha campanha, int dias){
        DateTime dateTime = new DateTime(campanha.getDtFim());
        campanha.setDtFim(dateTime.plusDays(dias).toDate());
    }

    public Campanha deleteCampanha(Long id) throws CampanhaNaoEncontradaException{
        Campanha campanha = campanhaRepository.findOne(id);
        try {
            campanhaRepository.delete(id);
        } catch (EmptyResultDataAccessException e) {
            throw new CampanhaNaoEncontradaException(id);
        }
        return campanha;
    }

    public Campanha updateCampanha(Long id, Campanha campanha) throws CampanhaNaoEncontradaException{
        Campanha campanhaPersisted = campanhaRepository.findOne(id);
        if (campanhaPersisted == null) {
            throw new CampanhaNaoEncontradaException(id);
        } else {
            campanha.setId(id);
        }
        return campanhaRepository.save(campanha);
    }

    public Iterable<Campanha> getValidCampanhaByTime(long time) {
        Date today = new Date();
        Iterable<Campanha> validCampanhaInVigencia = campanhaRepository.findValidCampanhaInVigencia(today);
        return StreamSupport
                .stream(validCampanhaInVigencia.spliterator(), false)
                .filter(campanha -> campanha.getTime().getId()==time)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public Object getAllCampanhas() {
        Iterable<Campanha> all = campanhaRepository.findAll();
        return all;
    }
}
