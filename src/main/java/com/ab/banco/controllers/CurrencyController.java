package com.ab.banco.controllers;

import com.ab.banco.dtos.CurrencyDTO;
import com.ab.banco.mappers.UserMapper;
import com.ab.banco.models.Currency;
import com.ab.banco.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/currencies")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private UserMapper userMapper;

    //traemos todas las divisas
    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<CurrencyDTO>> getAllCurrencies(){
        //obtenemos todas las entidades de moneda
        List<Currency> currencies = currencyService.getAllCurrencies();
        //convertimos las entidades en dto
        List<CurrencyDTO> currencyDTOS = currencies.stream().map(userMapper::currencyToCurrencyDTO).collect(Collectors.toList());
        return ResponseEntity.ok(currencyDTOS);
    }

    //obtenemos la moneda por el Id
    @CrossOrigin
    @GetMapping(value = "/{id}")
    public ResponseEntity<CurrencyDTO> getCurrencyById(@PathVariable Long id){
        //obtenemos la moneda desde el servicio
        Optional<Currency> currency = currencyService.getCurrencyById(id);
        return currency.map(currency1 -> ResponseEntity.ok(userMapper.currencyToCurrencyDTO(currency1))).orElse(ResponseEntity.notFound().build());
    }

    //creamos una moneda
    @CrossOrigin
    @PostMapping
    public ResponseEntity<CurrencyDTO> createCurrency(@RequestBody CurrencyDTO currencyDTO){
        //convertimos el dto en una entidad
        Currency currency = userMapper.currencyDTOToCurrency(currencyDTO);
        //creamos la moneda
        Currency createCurrency = currencyService.CreateCurrency(currency);
        //convertimos la entidad a dto para enviarla al cliente
        CurrencyDTO createCurrencyDTO = userMapper.currencyToCurrencyDTO(createCurrency);
        return ResponseEntity.status(HttpStatus.CREATED).body(createCurrencyDTO);
    }
    //modificamo una moneda
    @CrossOrigin
    @PutMapping(value = "/{id}")
    public ResponseEntity<CurrencyDTO> updateCurrency(@PathVariable Long id, @RequestBody CurrencyDTO currencyDTO){
        //convertimos el dto en una entidad
        Currency currency = userMapper.currencyDTOToCurrency(currencyDTO);
        //actualizamos la moneda
        Currency updateCurrncy = currencyService.updateCurrency(id,currency);
        //convertimos la entidad en dto
        CurrencyDTO updateCurrencyDTO = userMapper.currencyToCurrencyDTO(updateCurrncy);
        return ResponseEntity.ok(updateCurrencyDTO);
    }

    //eliminamos una divisa
    @CrossOrigin
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteCurrency(@PathVariable Long id){
     currencyService.deleteCurrency(id);
     return ResponseEntity.noContent().build();
    }
}
