package com.ab.banco.service;

import com.ab.banco.models.Currency;
import com.ab.banco.repository.CurrencyRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CurrencyService {
    @Autowired
    private CurrencyRepository currencyRepository;

    //traemos todas las divisas
   public List<Currency> getAllCurrencies(){
       return currencyRepository.findAll();
   }

   //traemos una divisa por id
   public Optional<Currency> getCurrencyById(Long id){
       return currencyRepository.findById(id);
   }

   //Creamos una nueva divisa
   public Currency CreateCurrency(Currency currency){
       //verificamos si hay duplicado
       if (currencyRepository.findByNameOrSymbol(currency.getName(), currency.getSymbol()).isPresent()){
           throw new RuntimeException("Ya existe una moneda con el nombre o simbolo");
       }

       return currencyRepository.save(currency);
   }

   //modificamos una divisa
    public Optional<Currency> updateCurrency(Long id, Currency currency){
       if (!currencyRepository.existsById(id)){
           return Optional.empty();
       }

       //verificamos si existe una moneda con el nombre o simbolo
        //si el nombre o el simbolo ya esta presente....
        if (currencyRepository.findByNameOrSymbol(currency.getName(), currency.getSymbol()).isPresent()){
            throw new RuntimeException("Ya existe una moneda con este nombre o simbolo");
        }
       currency.setId(id);
       return Optional.of(currencyRepository.save(currency));
    }

    //eliminamos una divisa
    public Boolean deleteCurrency(Long id){
       if (currencyRepository.existsById(id)){
           currencyRepository.deleteById(id);
           return true;
       }
       return false;
    }
}
