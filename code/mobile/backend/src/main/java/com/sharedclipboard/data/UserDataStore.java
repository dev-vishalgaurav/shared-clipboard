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
    * returns -1 if email already in use
    * returns 1 if success
     */
    public static int createNewUser(String username, String password) {
        Filter propertyFilter = new FilterPredicate("username", FilterOperator.EQUAL, username);
        Query q = new Query("User").setFilter(propertyFilter);

        List<Entity> results = dataStore.prepare(q).asList(FetchOptions.Builder.withDefaults());

        //fail if username already exists
        if(results.size() > 0)
            return -1;

        Entity entry = new Entity("User", username);
        entry.setProperty("username", username);
        entry.setProperty("password", password);
        entry.setProperty("passcode", generateRandomPasscode());
        dataStore.put(entry);
        return 1;

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
    *returns true if the username/password combo is valid
    * false if invalid
     */
    public static boolean correctUsernamePassword(String username, String password) {
        Filter propertyFilter = new FilterPredicate("username", FilterOperator.EQUAL, username);
        Query q = new Query("User").setFilter(propertyFilter);

        List<Entity> results = dataStore.prepare(q).asList(FetchOptions.Builder.withDefaults());

        if(results.size() != 0)
            return false;

        Entity userEntity = results.get(0);
        String entityPassword = (String) userEntity.getProperty("password");
        if(password.equals(entityPassword))
            return true;

        return false;
    }
}

