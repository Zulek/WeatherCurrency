package tz;

import javax.servlet.annotation.WebServlet;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Binder;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import tz.model.*;

import tz.services.CurrencyService;
import tz.services.TemperatureService;


import java.util.ArrayList;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme(ValoTheme.THEME_NAME)
public class MyUI extends UI {

    VerticalLayout temperatureUI() {
        NativeSelect<City> citiesDDMenu = new NativeSelect<>("Город");
        ArrayList<City> citiesArray = City.getCities();
        citiesDDMenu.setItems(citiesArray);
        citiesDDMenu.setItemCaptionGenerator(City::getName);
        citiesDDMenu.setEmptySelectionAllowed(false);
        citiesDDMenu.setSelectedItem(citiesArray.get(0));

        Forecast initForecast = TemperatureService.getInstance().fetchForecast(citiesArray.get(0).getId());

        Binder<ForecastTemp> todayBinder = new Binder<>();
        TextField todayTemperatureField = new TextField("Температура сегодня");
        todayTemperatureField.setReadOnly(true);
        todayBinder.forField(todayTemperatureField).bind(ForecastTemp::getDay, ForecastTemp::setDay);
        todayBinder.readBean(initForecast.getList().get(0).getTemp());

        Binder<ForecastTemp> tommorowBinder = new Binder<>();
        TextField tommorowTemperatureField = new TextField("Температура завтра");
        tommorowTemperatureField.setReadOnly(true);
        tommorowBinder.forField(tommorowTemperatureField).bind(ForecastTemp::getDay, ForecastTemp::setDay);
        tommorowBinder.readBean(initForecast.getList().get(1).getTemp());


        Button refreshTempButton = new Button("Обновить");
        refreshTempButton.addClickListener(event -> {
            Forecast fo = TemperatureService.getInstance().fetchForecast(citiesDDMenu.getSelectedItem().get().getId());
            todayBinder.readBean(fo.getList().get(0).getTemp());
            tommorowBinder.readBean(fo.getList().get(1).getTemp());
        });

        return new VerticalLayout( citiesDDMenu, todayTemperatureField, tommorowTemperatureField, refreshTempButton);

    }

    VerticalLayout currencyUI() {

        Currency initUSD = CurrencyService.getInstance().fetchCurrency("USD");
        Currency initEUR = CurrencyService.getInstance().fetchCurrency("EUR");

        Binder<Rates> todayUSD = new Binder<>();
        TextField todayUSDField = new TextField("USD");
        todayUSDField.setReadOnly(true);
        todayUSD.forField(todayUSDField).bind(Rates::getRub, Rates::setRub);
        todayUSD.readBean(initUSD.getRates());

        Binder<Rates> todayEUR = new Binder<>();
        TextField todayEURField = new TextField("EUR");
        todayEURField.setReadOnly(true);
        todayEUR.forField(todayEURField).bind(Rates::getRub, Rates::setRub);
        todayEUR.readBean(initEUR.getRates());

        Button refreshCurrencyButton = new Button("Обновить");
        refreshCurrencyButton.addClickListener(event -> {
            Currency newUSD = CurrencyService.getInstance().fetchCurrency("USD");
            Currency newEUR = CurrencyService.getInstance().fetchCurrency("EUR");
            todayUSD.readBean(newUSD.getRates());
            todayEUR.readBean(newEUR.getRates());
        });

        return new VerticalLayout( todayUSDField,todayEURField, refreshCurrencyButton);

    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        VerticalLayout tempLayout = temperatureUI();
        VerticalLayout currLayout = currencyUI();

        HorizontalLayout layout = new HorizontalLayout(tempLayout,currLayout);
        setContent(layout);

    }



    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
