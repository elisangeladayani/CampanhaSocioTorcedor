package com.backoffice.usecases;

import com.backoffice.entities.Time;
import com.backoffice.gateway.repository.TimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeService {

    @Autowired
    private TimeRepository timeRepository;

    public Iterable<Time> getAllTimes(){
        return timeRepository.findAll();
    }
}
