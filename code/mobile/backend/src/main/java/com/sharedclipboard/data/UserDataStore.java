package com.sharedclipboard.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

import java.util.List;
import java.util.Random;

/**
 * Created by Girouard23 on 5/24/16.
 */
public class UserDataStore {

    private static final DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();

    /*
    * Creates new user
    * returns "" if the username is already in use
    * returns passcode if creating the user was successful
     */
    public static String createNewUser(String username, String password) {
        Filter propertyFilter = new FilterPredicate("username", FilterOperator.EQUAL, username);
        Query q = new Query("User").setFilter(propertyFilter);

        List<Entity> results = dataStore.prepare(q).asList(FetchOptions.Builder.withDefaults());

        //fail if username already exists
        if(results.size() > 0)
            return "";

        Entity entry = new Entity("User", username);
        entry.setProperty("username", username);
        entry.setProperty("password", password);
        String passcode = generateRandomPasscode();
        entry.setProperty("passcode", passcode);
        dataStore.put(entry);
        return passcode;

    }

    private static String generateRandomPasscode() {
        Random random = new Random();
        String randomString = "";

        char[] word = new char[5];
        while (true) {
            for (int j = 0; j < word.length; j++) {
                word[j] = (char) ('a' + random.nextInt(26));
            }
            randomString = new String(word);

            Filter propertyFilter = new FilterPredicate("passcode", FilterOperator.EQUAL, randomString);
            Query q = new Query("User").setFilter(propertyFilter);

            //check if passcode is unique
            if(dataStore.prepare(q).asList(FetchOptions.Builder.withDefaults()).size() == 0)
                break;
        }

        return randomString;
    }

    /*
    * returns passcode if the username/password combo is valid
    * returns "" if invalid
     */
    public static String correctUsernamePassword(String username, String password) {
        Filter propertyFilter = new FilterPredicate("username", FilterOperator.EQUAL, username);
        Query q = new Query("User").setFilter(propertyFilter);

        List<Entity> results = dataStore.prepare(q).asList(FetchOptions.Builder.withDefaults());

        if(results.size() != 1)
            return "";

        Entity userEntity = results.get(0);
        String entityPassword = (String) userEntity.getProperty("password");
        String passcode = (String) userEntity.getProperty("passcode");
        if(password.equals(entityPassword))
            return passcode;

        return "";
    }
}

