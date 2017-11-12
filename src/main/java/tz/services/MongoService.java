package tz.services;

import com.mongodb.*;

import com.mongodb.client.MongoDatabase;

import org.slf4j.*;
import org.bson.Document;

public class MongoService  {

    private static final Logger LOGGER = LoggerFactory.getLogger("tzlogger");
    private static MongoService mongoService;
    MongoDatabase db;

    public MongoService() throws Exception {
        LOGGER.debug("MongoService creation");
        MongoClientOptions.Builder o = MongoClientOptions.builder().connectTimeout(100);
        MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", 27017), o.build());

        try {
            mongoClient.getAddress();
        } catch (Exception e) {
            LOGGER.info("Couldn't connect to MongoDB");
            mongoClient.close();
            throw e;
        }

        db = mongoClient.getDatabase("mydb");

    }
    public static MongoService getInstance() throws Exception {
        if (mongoService == null) {
            mongoService = new MongoService();
        }
        return mongoService;
    }

    public Long fetchPagevisits() {
        LOGGER.debug("Fetching page visits");
        return db.getCollection("visits").count();
    }

    public void insertPagevisit() {
        LOGGER.debug("Inserting new page visit");
        db.getCollection("visits").insertOne(Document.parse("{visit: 1}"));
    }
}
