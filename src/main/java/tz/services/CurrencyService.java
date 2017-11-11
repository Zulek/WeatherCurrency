package tz.services;

import org.glassfish.jersey.jackson.JacksonFeature;
import tz.model.Currency;
import tz.model.Forecast;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class CurrencyService {

    private final static CurrencyService instance = new CurrencyService();

    private final static WebTarget currencyResource = ClientBuilder.newBuilder()
            .register(JacksonFeature.class).build()
            .target("https://api.fixer.io/latest");

    public static CurrencyService getInstance() {
        return instance;
    }

    public Currency fetchCurrency(String base) {
        return currencyResource
                .queryParam("base", base)
                .request(MediaType.APPLICATION_JSON).get(Currency.class);
    }

}