package tz.services;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.*;
import org.glassfish.jersey.jackson.JacksonFeature;

import java.util.Map;

public class JsonService {

    private static final Logger LOGGER = LoggerFactory.getLogger("tzlogger");

    private WebTarget webTarget;

    public JsonService(String targetURL){
        Client client = ClientBuilder.newBuilder()
                .register(JacksonFeature.class).build();
        webTarget = client.target(targetURL);
        LOGGER.debug("JsonService created: " + webTarget.getUri());
    }

    public Response fetch(Map<String,Object> parameters) {
        LOGGER.debug("Fetching with parameters: " + parameters);
        WebTarget wt = webTarget;
        for (Map.Entry<String,Object> parameter: parameters.entrySet()) {
            wt = wt.queryParam(parameter.getKey(), parameter.getValue());
        }
        return wt.request(MediaType.APPLICATION_JSON).get();
    }

}