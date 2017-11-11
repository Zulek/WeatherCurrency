package tz.model;

import java.util.ArrayList;

public class City {

    private final String id;
    private final String name;

    public City(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static ArrayList<City> getCities() {
        ArrayList<City> cities = new ArrayList<>();

        City city = new City("498817", "Санкт-Петербург");
        cities.add(city);

        city = new City("524901", "Москва");
        cities.add(city);

        city = new City("1496747", "Новосибирск");
        cities.add(city);

        return cities;
    }

}