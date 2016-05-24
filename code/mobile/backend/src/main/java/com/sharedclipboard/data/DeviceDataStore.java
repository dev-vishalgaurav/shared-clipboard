package com.sharedclipboard.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Girouard23 on 5/24/16.
 */
public class DeviceDataStore {

    private static final DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();

    public static void insertDevice(String deviceKey, String email) {
        Query.Filter propertyFilter = new Query.FilterPredicate("deviceKey", Query.FilterOperator.EQUAL, deviceKey);
        Query q = new Query("Device").setFilter(propertyFilter);

        List<Entity> results = dataStore.prepare(q).asList(FetchOptions.Builder.withDefaults());

        //return if device already exists
        if(results.size() > 0)
            return;

        Entity entry = new Entity("Device", deviceKey);
        entry.setProperty("deviceKey", deviceKey);
        entry.setProperty("email", email);
        dataStore.put(entry);
    }

    /*
    *returns all device key strings associated with a specific email
     */
    public static ArrayList<String> allDeviceKeys(String email) {
        ArrayList<String> retList = new ArrayList<String>();

        Query.Filter propertyFilter = new Query.FilterPredicate("email", Query.FilterOperator.EQUAL, email);
        Query q = new Query("Device").setFilter(propertyFilter);

        List<Entity> results = dataStore.prepare(q).asList(FetchOptions.Builder.withDefaults());

        String deviceKey;
        for(Entity entity : results) {
            deviceKey = (String) entity.getProperty("deviceKey");
            retList.add(deviceKey);
        }

        return retList;
    }
}
