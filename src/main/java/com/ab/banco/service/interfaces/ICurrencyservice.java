package com.ab.banco.service.interfaces;

import com.ab.banco.persistence.models.Currency;

import java.util.List;
import java.util.Optional;

public interface ICurrencyservice {

        //traemos todas las divisas
        public List<Currency> getAllCurrencies();

        //traemos una divisa por id
        public Optional<Currency> getCurrencyById(Long id);

        //creamos una nueva divisa
        public Currency CreateCurrency(Currency currency);

        //modificamos una divisa
        public Currency updateCurrency(Long id, Currency currency);

        //eliminamos una divisa
        public void deleteCurrency(Long id);
}
