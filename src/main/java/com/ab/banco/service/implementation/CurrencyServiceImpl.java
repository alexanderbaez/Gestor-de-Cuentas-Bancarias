package com.ab.banco.service.implementation;

import com.ab.banco.persistence.models.Currency;
import com.ab.banco.persistence.repository.CurrencyRepository;
import com.ab.banco.service.interfaces.ICurrencyservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CurrencyServiceImpl implements ICurrencyservice {
    @Autowired
    private CurrencyRepository currencyRepository;

    //traemos todas las divisas
   public List<Currency> getAllCurrencies(){
       List<Currency> currencies = currencyRepository.findAll();
       //verificamos
       if (currencies.isEmpty()){
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay divisas");
       }
       return currencies;
   }

   //traemos una divisa por id
   public Optional<Currency> getCurrencyById(Long id){
       return currencyRepository.findById(id);
   }

   //Creamos una nueva divisa
   public Currency CreateCurrency(Currency currency){
       //verificamos si hay duplicado
       if (currencyRepository.findByNameOrSymbol(currency.getName(), currency.getSymbol()).isPresent()){
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Ya existe una divisa con el nombre o simbolo");
       }
       return currencyRepository.save(currency);
   }

   //modificamos una divisa
    public Currency updateCurrency(Long id, Currency currency){
       //verificamos divisa
       if (!currencyRepository.existsById(id)){
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Divisa no encontrada");
       }
       //verificamos si existe una moneda con el nombre o simbolo
        //si el nombre o el simbolo ya esta presente....
        if (currencyRepository.findByNameOrSymbol(currency.getName(), currency.getSymbol()).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe una divisa con nombre o simbolo");
        }
       currency.setId(id);
       return currencyRepository.save(currency);
    }

    //eliminamos una divisa
    public void deleteCurrency(Long id){
       if (currencyRepository.existsById(id)){
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Divisa no encontrada");
       }
       currencyRepository.deleteById(id);
    }
}
