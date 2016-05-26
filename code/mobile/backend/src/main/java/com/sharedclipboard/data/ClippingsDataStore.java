package com.sharedclipboard.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Girouard23 on 5/24/16.
 */
public class ClippingsDataStore {

    private static final DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();

    public static void addClipping(Clipping clipping, String passcode) {
        Entity entry = new Entity("Clipping");
        entry.setProperty("passcode", passcode);
        entry.setProperty("text", clipping.text);
        entry.setProperty("dateString", clipping.dateString);
        entry.setProperty("dateTime", clipping.dateTime);
        dataStore.put(entry);
    }

    public static ArrayList<Clipping> getAllClippingsWithPasscode(String passcode) {
        Query.Filter propertyFilter = new Query.FilterPredicate("passcode", Query.FilterOperator.EQUAL, passcode);
        Query q = new Query("Clipping").setFilter(propertyFilter);
        //q.addSort("dateTime", Query.SortDirection.ASCENDING);

        List<Entity> results = dataStore.prepare(q).asList(FetchOptions.Builder.withDefaults());

        ArrayList<Clipping> retClippings = new ArrayList<Clipping>();

        for(Entity entity : results) {
            String text = (String) entity.getProperty("text");
            Long dateTime = (Long) entity.getProperty("dateTime");
            Clipping clipping = new Clipping(dateTime, text);
            retClippings.add(clipping);
        }

        Collections.sort(retClippings);

        return retClippings;
    }
}
