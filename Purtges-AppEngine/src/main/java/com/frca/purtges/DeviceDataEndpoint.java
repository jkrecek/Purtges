package com.frca.purtges;

import com.frca.purtges.Const.Ids;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.users.User;
import com.google.appengine.datanucleus.query.JPACursorHelper;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Api(
    name = "devicedataendpoint",
    namespace = @ApiNamespace(ownerDomain = "frca.com", ownerName = "frca.com", packagePath = Ids.PACKAGE_NAME),
    clientIds = {Ids.WEB_CLIENT_ID, Ids.ANDROID_CLIENT_ID},
    audiences = {Ids.ANDROID_AUDIENCE}
)

public class DeviceDataEndpoint {

    @SuppressWarnings({"unchecked", "unused"})
    @ApiMethod(name = "listDeviceData")
    public CollectionResponse<DeviceData> listDeviceData(
        @Nullable @Named("cursor") String cursorString,
        @Nullable @Named("limit") Integer limit) {

        EntityManager mgr = null;
        List<DeviceData> execute = null;

        try {
            mgr = getEntityManager();
            Query query = mgr.createQuery("select from DeviceData as DeviceData");
            Cursor cursor;
            if (cursorString != null && cursorString.trim().length() > 0) {
                cursor = Cursor.fromWebSafeString(cursorString);
                query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
            }

            if (limit != null) {
                query.setFirstResult(0);
                query.setMaxResults(limit);
            }

            execute = (List<DeviceData>) query.getResultList();
            cursor = JPACursorHelper.getCursor(execute);
            if (cursor != null) cursorString = cursor.toWebSafeString();

            // Tight loop for fetching all entities from datastore and accomodate
            // for lazy fetch.
            for (DeviceData obj : execute) ;
        } finally {
            if (mgr != null) {
                mgr.close();
            }
        }

        return CollectionResponse.<DeviceData>builder()
            .setItems(execute)
            .setNextPageToken(cursorString)
            .build();
    }

    @ApiMethod(name = "getDeviceData")
    public DeviceData getDeviceData(@Named("id") String id) {
        EntityManager mgr = getEntityManager();
        DeviceData deviceData = null;
        try {
            deviceData = mgr.find(DeviceData.class, id);
        } finally {
            mgr.close();
        }
        return deviceData;
    }

    @ApiMethod(name = "insertDeviceData")
    public DeviceData insertDeviceData(DeviceData deviceData, User user) {
        EntityManager mgr = getEntityManager();
        try {
            UserDataEndpoint userDataEndpoint = new UserDataEndpoint();
            UserData userData = userDataEndpoint.findUserData(user);

            if (userData == null)
                return null;

            if (containsDeviceData(deviceData)) {
                throw new EntityExistsException("Object already exists");
            }

            deviceData.setOwner(userData.getEmail());
            mgr.persist(deviceData);
        } finally {
            mgr.close();
        }
        return deviceData;
    }

    @ApiMethod(name = "removeDeviceData")
    public DeviceData removeDeviceData(@Named("id") String id) {
        EntityManager mgr = getEntityManager();
        DeviceData deviceData = null;
        try {
            deviceData = mgr.find(DeviceData.class, id);
            mgr.remove(deviceData);
        } finally {
            mgr.close();
        }
        return deviceData;
    }

    private boolean containsDeviceData(DeviceData deviceData) {
        EntityManager mgr = getEntityManager();
        boolean contains = true;
        try {
            DeviceData item = mgr.find(DeviceData.class, deviceData.getRegistrationId());
            if (item == null) {
                contains = false;
            }
        } finally {
            mgr.close();
        }
        return contains;
    }

    private static EntityManager getEntityManager() {
        return EMF.get().createEntityManager();
    }

}
