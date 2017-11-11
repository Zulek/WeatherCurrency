package tz.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ForecastWeather {

    private Long dt;
    private ForecastTemp temp;

    public Long getDt() {
        return dt;
    }

    public void setDt(Long dt) {
        this.dt = dt;
    }

    public ForecastTemp getTemp() {
        return temp;
    }

    public void setTemp(ForecastTemp temp) {
        this.temp = temp;
    }
}