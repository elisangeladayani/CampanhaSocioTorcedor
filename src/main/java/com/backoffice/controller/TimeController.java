package com.backoffice.controller;

import com.backoffice.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TimeController {

    @Autowired
    private TimeService timeService;

    @GetMapping("/api/time/all")
    public Object getAllTimes(){
        return timeService.getAllTimes();
    }
}
