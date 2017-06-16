package com.backoffice.service;

import com.backoffice.model.Time;
import com.backoffice.repository.TimeRepository;
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
