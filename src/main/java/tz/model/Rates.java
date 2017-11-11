package tz.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Rates {

    private String RUB;

    @JsonGetter("RUB")
    public String getRub() {
        return RUB;
    }

    public void setRub(String rub) {
        this.RUB = rub;
    }
}
