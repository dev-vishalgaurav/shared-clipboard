package com.sharedclipboard.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.sharedclipboard.RegistrationRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Girouard23 on 5/24/16.
 */
public class DeviceDataStore {

    private static final DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();

    public static void insertDevice(String deviceKey, String passcode) {
        Query.Filter propertyFilter = new Query.FilterPredicate("deviceKey", Query.FilterOperator.EQUAL, deviceKey);
        Query q = new Query("Device").setFilter(propertyFilter);

        List<Entity> results = dataStore.prepare(q).asList(FetchOptions.Builder.withDefaults());

        for(Entity entity : results) {
            String entityPasscode = (String) entity.getProperty("passcode");
            //check if passcode, deviceKey combo already exists
            if(entityPasscode.equals(passcode))
                return;
        }

        Entity entry = new Entity("Device");
        entry.setProperty("deviceKey", deviceKey);
        entry.setProperty("passcode", passcode);
        dataStore.put(entry);
    }

    /*
    *returns all device key strings associated with a specific passcode
     */
    public static ArrayList<RegistrationRecord> allDeviceRegistrationRecords(String passcode) {
        ArrayList<RegistrationRecord> retList = new ArrayList<RegistrationRecord>();

        Query.Filter propertyFilter = new Query.FilterPredicate("passcode", Query.FilterOperator.EQUAL, passcode);
        Query q = new Query("Device").setFilter(propertyFilter);

        List<Entity> results = dataStore.prepare(q).asList(FetchOptions.Builder.withDefaults());

        String deviceKey;
        for(Entity entity : results) {
            deviceKey = (String) entity.getProperty("deviceKey");
            RegistrationRecord record = new RegistrationRecord();
            record.setRegId(deviceKey);
            retList.add(record);
        }

        return retList;
    }
}
