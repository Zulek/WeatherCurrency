package tz.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Forecast {

    private List<ForecastWeather> list;

    public List<ForecastWeather> getList() {
        return list;
    }

    public void setList(List<ForecastWeather> list) {
        this.list = list;
    }
}
