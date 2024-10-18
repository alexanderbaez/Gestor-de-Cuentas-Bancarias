package com.ab.banco.controllers;

import com.ab.banco.models.Currency;
import com.ab.banco.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/currencies")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @CrossOrigin
    @GetMapping
    public List<Currency> getAllCurrencies(){
        return currencyService.getAllCurrencies();
    }

    @CrossOrigin
    @GetMapping(value = "/{id}")
    public ResponseEntity<Currency> getCurrencyById(@PathVariable Long id){
        return currencyService.getCurrencyById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @CrossOrigin
    @PostMapping(value = "/currency")
    public ResponseEntity<Currency> createCurrency(@RequestBody Currency currency){
        Currency createCurrency = currencyService.CreateCurrency(currency);
        return ResponseEntity.status(HttpStatus.CREATED).body(createCurrency);
    }

    @CrossOrigin
    @PutMapping(value = "/{id}")
    public ResponseEntity<Currency> updateCurrency(@PathVariable Long id, @RequestBody Currency currency){
        return currencyService.updateCurrency(id, currency).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @CrossOrigin
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Currency> deleteCurrency(@PathVariable Long id){
        if (currencyService.deleteCurrency(id)){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
