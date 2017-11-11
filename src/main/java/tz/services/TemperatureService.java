package tz.services;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.glassfish.jersey.jackson.JacksonFeature;
import tz.model.Forecast;

public class TemperatureService {

    private String apikey = ConfigResolver.getPropertyValue("weathermap.apikey");
    private final static TemperatureService instance = new TemperatureService();

    private final static WebTarget forecastResource = ClientBuilder.newBuilder()
            .register(JacksonFeature.class).build()
            .target("http://api.openweathermap.org/data/2.5/forecast/daily");

    public static TemperatureService getInstance() {
        return instance;
    }

    public Forecast fetchForecast(String city) {
        return forecastResource
                .queryParam("id", city)
                .queryParam("APPID", apikey)
                .queryParam("units", "metric")
                .request(MediaType.APPLICATION_JSON).get(Forecast.class);
    }

}