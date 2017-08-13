package com.backoffice.gateway.repository;

import com.backoffice.entities.Campanha;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

public interface CampanhaRepository extends CrudRepository<Campanha, Long>{

    @Query("select c from Campanha c where c.dtInicio>=?1")
    Iterable<Campanha> findAllCampanhaInVigencia(Date dtInicio);

    @Query("select c from Campanha c where c.dtInicio<=?1 and c.dtFim>=?1")
    Iterable<Campanha> findValidCampanhaInVigencia(Date dt);

    Iterable<Campanha> findAllByTimeAndDtInicioAfterAndDtFimBefore(long time, Date dtInicio, Date dtFim);
}