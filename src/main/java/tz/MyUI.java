package tz;

import javax.servlet.annotation.WebServlet;
import javax.ws.rs.core.Response;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import tz.model.*;

import tz.services.MongoService;
import tz.services.JsonService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.*;
/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme(ValoTheme.THEME_NAME)
public class MyUI extends UI {

    private static final Logger LOGGER = LoggerFactory.getLogger("tzlogger");

    GridLayout temperatureUI() {
        LOGGER.debug("Temperature UI construction started");

        GridLayout tempGridLayout = new GridLayout(1,4);

        NativeSelect<City> citiesDDMenu = new NativeSelect<>("Город");
        ArrayList<City> citiesArray = City.getCities();
        citiesDDMenu.setItems(citiesArray);
        citiesDDMenu.setItemCaptionGenerator(City::getName);
        citiesDDMenu.setEmptySelectionAllowed(false);
        citiesDDMenu.setSelectedItem(citiesArray.get(0));

        TextField todayTemperatureField = new TextField("Температура сегодня");
        todayTemperatureField.setReadOnly(true);

        TextField tomorrowTemperatureField = new TextField("Температура завтра");
        tomorrowTemperatureField.setReadOnly(true);

        JsonService weatherService = new JsonService("https://api.openweathermap.org/data/2.5/forecast/daily");
        Map<String, Object> forecastParameters = new HashMap<>();

        forecastParameters.put("APPID", ConfigResolver.getPropertyValue("weathermap.apikey"));
        forecastParameters.put("units", "metric");

        Button refreshTempButton = new Button("Обновить");
        refreshTempButton.addClickListener(event -> {
            LOGGER.debug("Temperature button clicked");

            forecastParameters.put("id", citiesDDMenu.getSelectedItem().get().getId());
            Response forecast = weatherService.fetch(forecastParameters);

            if (forecast.getStatus() == Response.Status.OK.getStatusCode()) {
                List<ForecastWeather> forecastWeatherList = forecast.readEntity(Forecast.class).getList();
                todayTemperatureField.setValue(forecastWeatherList.get(0).getTemp().getDay());
                tomorrowTemperatureField.setValue(forecastWeatherList.get(1).getTemp().getDay());
            }
            else {
                LOGGER.info("Couldn't reach weather API");
                todayTemperatureField.setValue("Не удалось получить");
                tomorrowTemperatureField.setValue("Не удалось получить");
            }
        });

        tempGridLayout.setSpacing(true);
        tempGridLayout.setMargin(true);
        tempGridLayout.addComponent(citiesDDMenu,0,0);
        tempGridLayout.setComponentAlignment(citiesDDMenu,Alignment.MIDDLE_CENTER);
        tempGridLayout.addComponent(todayTemperatureField,0,1);
        tempGridLayout.setComponentAlignment(todayTemperatureField,Alignment.MIDDLE_CENTER);
        tempGridLayout.addComponent(tomorrowTemperatureField,0,2);
        tempGridLayout.setComponentAlignment(tomorrowTemperatureField,Alignment.MIDDLE_CENTER);
        tempGridLayout.addComponent(refreshTempButton,0,3);
        tempGridLayout.setComponentAlignment(refreshTempButton,Alignment.MIDDLE_CENTER);
        LOGGER.debug("Temperature UI construction ended");

        return tempGridLayout;
    }

    GridLayout currencyUI() {
        LOGGER.debug("Currency UI construction started");

        GridLayout currGridLayout = new GridLayout(1,3);

        TextField todayUSDField = new TextField("USD");
        todayUSDField.setReadOnly(true);

        TextField todayEURField = new TextField("EUR");
        todayEURField.setReadOnly(true);

        JsonService currencyService = new JsonService("https://api.fixer.io/latest");
        Map<String, Object> USDParameters = new HashMap<>();
        USDParameters.put("base", "USD");
        Map<String, Object> EURParameters = new HashMap<>();
        EURParameters.put("base", "EUR");

        Button refreshCurrencyButton = new Button("Обновить");
        refreshCurrencyButton.addClickListener(event -> {
            LOGGER.debug("Currency button clicked");

            Response newUSD = currencyService.fetch(USDParameters);
            Response newEUR = currencyService.fetch(EURParameters);

            if (newUSD.getStatus() == Response.Status.OK.getStatusCode()) {
                todayUSDField.setValue(newUSD.readEntity(Currency.class).getRates().getRub());
            }
            else {
                LOGGER.info("Couldn't reach currency API for USD");
                todayUSDField.setValue("Не удалось получить");
            }

            if (newEUR.getStatus() == Response.Status.OK.getStatusCode()) {
                todayEURField.setValue(newEUR.readEntity(Currency.class).getRates().getRub());
            }
            else {
                LOGGER.info("Couldn't reach currency API for EUR");
                todayEURField.setValue("Не удалось получить");
            }
        });

        currGridLayout.setSpacing(true);
        currGridLayout.setMargin(true);
        currGridLayout.addComponent(todayUSDField,0,0);
        currGridLayout.setComponentAlignment(todayUSDField,Alignment.MIDDLE_CENTER);
        currGridLayout.addComponent(todayEURField,0,1);
        currGridLayout.setComponentAlignment(todayEURField,Alignment.MIDDLE_CENTER);
        currGridLayout.addComponent(refreshCurrencyButton,0,2);
        currGridLayout.setComponentAlignment(refreshCurrencyButton,Alignment.MIDDLE_CENTER);

        LOGGER.debug("Currency UI construction ended");

        return currGridLayout;

    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        LOGGER.debug("Main UI construction started");

        setStyleName("mytheme");
        Label ipLabel = new Label("Ваш IP " + getPage().getWebBrowser().getAddress());
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Label timeLabel = new Label("Информация по состоянию на " + ldt.format(formatter));
        GridLayout labelGridLayout = new GridLayout(3,1);
        labelGridLayout.addComponent(timeLabel,0,0);
        labelGridLayout.setComponentAlignment(timeLabel,Alignment.BOTTOM_LEFT);
        labelGridLayout.addComponent(ipLabel,2,0);
        labelGridLayout.setComponentAlignment(ipLabel,Alignment.BOTTOM_RIGHT);
        labelGridLayout.setSpacing(true);
        labelGridLayout.setMargin(true);


        TextField pageVisitsField = new TextField("Посещения сайта");
        pageVisitsField.setReadOnly(true);

        try {
            MongoService mongoService = MongoService.getInstance();
            mongoService.insertPagevisit();
            pageVisitsField.setValue(String.valueOf(mongoService.fetchPagevisits()));
            LOGGER.debug("Connected to MongoDB");
        }
        catch (Exception e) {
            LOGGER.info("Couldn't reach MongoDB: " + e);
            pageVisitsField.setValue("Не удалось получить");
        }

        GridLayout tempLayout = temperatureUI();
        GridLayout currLayout = currencyUI();
        VerticalLayout visitLayout = new VerticalLayout(pageVisitsField);

        GridLayout tcvLayout = new GridLayout(3,1, tempLayout,currLayout,visitLayout);
        tcvLayout.setSpacing(true);
        tcvLayout.setComponentAlignment(tempLayout,Alignment.MIDDLE_CENTER);
        tcvLayout.setComponentAlignment(currLayout,Alignment.MIDDLE_CENTER);
        tcvLayout.setComponentAlignment(visitLayout,Alignment.MIDDLE_CENTER);
        GridLayout gridLayout = new GridLayout(1,3);
        HorizontalLayout titleLayout = new HorizontalLayout(new Label("Тестовое сетевое приложение"));
        titleLayout.setSpacing(true);
        titleLayout.setMargin(true);
        gridLayout.addComponent(titleLayout,0,0);
        gridLayout.setComponentAlignment(titleLayout, Alignment.TOP_CENTER);
        gridLayout.addComponent(tcvLayout,0,1);
        gridLayout.setComponentAlignment(tcvLayout, Alignment.MIDDLE_CENTER);
        gridLayout.addComponent(labelGridLayout, 0,2);
        gridLayout.setComponentAlignment(labelGridLayout,Alignment.BOTTOM_CENTER);

        gridLayout.setSizeFull();
        setContent(gridLayout);

        LOGGER.debug("Main UI construction ended");

    }



    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = true)
    public static class MyUIServlet extends VaadinServlet {
    }
}
