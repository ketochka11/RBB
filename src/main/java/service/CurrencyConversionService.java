package service;

import entity.Currency;
import service.impl.NbrbCurrencyConversionService;

public interface CurrencyConversionService {

    static CurrencyConversionService getInstance() {
        return new NbrbCurrencyConversionService();
    }

    double getConversionRatio(Currency original, Currency target);
}