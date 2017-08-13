package com.backoffice.http;

import com.backoffice.entities.exception.CampanhaExpiradaException;
import com.backoffice.entities.exception.CampanhaNaoEncontradaException;
import com.backoffice.entities.Campanha;
import com.backoffice.usecases.CampanhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class CampanhaController {

    @Autowired
    private CampanhaService campanhaService;

    @GetMapping(value = "/api/campanha/{id}")
    public Campanha getValidCampanhaById(@PathVariable("id") Long id) throws CampanhaExpiradaException, CampanhaNaoEncontradaException{
        return campanhaService.getValidCampanhaById(id);
    }

    @GetMapping(value = "/api/campanha/all")
    public Object getAllCampanhas() {
        return campanhaService.getAllCampanhas();
    }

    @PostMapping(value = "/api/campanha", consumes = "application/json;charset=UTF-8")
    public Campanha createCampanha(@RequestBody @Valid Campanha campanha) throws IllegalArgumentException {
        return campanhaService.createCampanha(campanha);
    }


    @DeleteMapping("/api/campanha/{id}")
    public Campanha deleteCampanha(@PathVariable("id") Long id) throws CampanhaNaoEncontradaException {
        return campanhaService.deleteCampanha(id);
    }

    @PutMapping("/api/campanha/{id}")
    public Campanha updateCampanha(@RequestBody @Valid Campanha campanha, @PathVariable("id") Long id) throws CampanhaNaoEncontradaException {
        return campanhaService.updateCampanha(id, campanha);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleException(CampanhaNaoEncontradaException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleException(CampanhaExpiradaException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleException(IllegalArgumentException ex) {
        return ex.getMessage();
    }

}
