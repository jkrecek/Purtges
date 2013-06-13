package com.frca.gamingscheduler;

import com.frca.gamingscheduler.Const.Ids;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;
import com.google.appengine.datanucleus.query.JPACursorHelper;

import java.io.IOException;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Api(
    name = "userdeviceendpoint",
    namespace = @ApiNamespace(ownerDomain = "frca.com", ownerName = "frca.com", packagePath = "gamingscheduler"),
    clientIds = {Ids.WEB_CLIENT_ID, Ids.ANDROID_CLIENT_ID},
    audiences = {Ids.ANDROID_AUDIENCE}
)
public class UserDeviceEndpoint {

    /**
     * This method lists all the entities inserted in datastore.
     * It uses HTTP GET method and paging support.
     *
     * @return A CollectionResponse class containing the list of all entities
     *         persisted and a cursor to the next page.
     */
    @SuppressWarnings({"unchecked", "unused"})
    @ApiMethod(name = "listUserDevice")
    public CollectionResponse<UserDevice> listUserDevice(
        @Nullable @Named("cursor") String cursorString,
        @Nullable @Named("limit") Integer limit) {

        EntityManager mgr = null;
        List<UserDevice> execute = null;

        try {
            mgr = getEntityManager();
            Query query = mgr.createQuery("select from UserDevice as UserDevice");
            Cursor cursor;
            if (cursorString != null && cursorString.trim().length() > 0) {
                cursor = Cursor.fromWebSafeString(cursorString);
                query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
            }

            if (limit != null) {
                query.setFirstResult(0);
                query.setMaxResults(limit);
            }

            execute = (List<UserDevice>) query.getResultList();
            cursor = JPACursorHelper.getCursor(execute);
            if (cursor != null) cursorString = cursor.toWebSafeString();

            // Tight loop for fetching all entities from datastore and accomodate
            // for lazy fetch.
            for (UserDevice obj : execute) ;
        } finally {
            if (mgr != null) {
                mgr.close();
            }
        }

        return CollectionResponse.<UserDevice>builder()
            .setItems(execute)
            .setNextPageToken(cursorString)
            .build();
    }

    /**
     * This method gets the entity having primary key id. It uses HTTP GET method.
     *
     * @param id the primary key of the java bean.
     * @return The entity with primary key id.
     */
    @ApiMethod(name = "getUserDevice")
    public UserDevice getUserDevice(@Named("id") String id) {
        EntityManager mgr = getEntityManager();
        UserDevice userDevice = null;
        try {
            userDevice = mgr.find(UserDevice.class, id);
        } finally {
            mgr.close();
        }
        return userDevice;
    }

    /**
     * This inserts a new entity into App Engine datastore. If the entity already
     * exists in the datastore, an exception is thrown.
     * It uses HTTP POST method.
     *
     * @param registrationId the registrationId of entity to be inserted.
     * @return The inserted entity.
     */
    @ApiMethod(name = "insertUserDevice")
    public UserDevice insertUserDevice(@Named("registrationId") String registrationId, User user) throws
        OAuthRequestException, IOException {
        if (user != null) {
            EntityManager mgr = getEntityManager();
            try {

                if (containsUserDevice(registrationId)) {
                    throw new EntityExistsException("Object already exists");
                }
                UserDevice reg = new UserDevice();
                reg.setDeviceRegID(registrationId);
                reg.setUserEmail(user.getEmail());
                mgr.persist(reg);
                return reg;
            } finally {
                mgr.close();
            }
        } else
            throw new OAuthRequestException("User not authorized!");
    }

    /**
     * This method is used for updating an existing entity. If the entity does not
     * exist in the datastore, an exception is thrown.
     * It uses HTTP PUT method.
     *
     * @param userDevice the entity to be updated.
     * @return The updated entity.
     */
    @ApiMethod(name = "updateUserDevice")
    public UserDevice updateUserDevice(UserDevice userDevice) {
        EntityManager mgr = getEntityManager();
        try {
            if (!containsUserDevice(userDevice)) {
                throw new EntityNotFoundException("Object does not exist");
            }
            mgr.persist(userDevice);
        } finally {
            mgr.close();
        }
        return userDevice;
    }

    /**
     * This method removes the entity with primary key id.
     * It uses HTTP DELETE method.
     *
     * @param id the primary key of the entity to be deleted.
     * @return The deleted entity.
     */
    @ApiMethod(name = "removeUserDevice")
    public UserDevice removeUserDevice(@Named("id") String id) {
        EntityManager mgr = getEntityManager();
        UserDevice userDevice = null;
        try {
            userDevice = mgr.find(UserDevice.class, id);
            mgr.remove(userDevice);
        } finally {
            mgr.close();
        }
        return userDevice;
    }

    private boolean containsUserDevice(String deviceId) {
        EntityManager mgr = getEntityManager();
        boolean contains = true;
        try {
            UserDevice item = mgr.find(UserDevice.class, deviceId);
            if (item == null) {
                contains = false;
            }
        } finally {
            mgr.close();
        }
        return contains;
    }

    private boolean containsUserDevice(UserDevice userDevice) {
        return containsUserDevice(userDevice.getDeviceRegID());
    }

    private static EntityManager getEntityManager() {
        return EMF.get().createEntityManager();
    }

}
