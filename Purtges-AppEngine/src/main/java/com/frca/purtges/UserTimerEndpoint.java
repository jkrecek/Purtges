package com.frca.purtges;

import com.frca.purtges.Const.Ids;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JPACursorHelper;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;

@Api(
    name = "usertimerendpoint",
    namespace = @ApiNamespace(ownerDomain = "frca.com", ownerName = "frca.com", packagePath = Ids.PACKAGE_NAME),
    clientIds = {Ids.WEB_CLIENT_ID, Ids.ANDROID_CLIENT_ID},
    audiences = {Ids.ANDROID_AUDIENCE}
)
public class UserTimerEndpoint {

    @SuppressWarnings({"unchecked", "unused"})
    @ApiMethod(name = "listUserTimer")
    public CollectionResponse<UserTimer> listUserTimer(
        @Nullable @Named("cursor") String cursorString,
        @Nullable @Named("limit") Integer limit) {

        EntityManager mgr = null;
        List<UserTimer> execute = null;

        try {
            mgr = getEntityManager();
            Query query = mgr.createQuery("select from UserTimer as UserTimer");
            Cursor cursor;
            if (cursorString != null && cursorString.trim().length() > 0) {
                cursor = Cursor.fromWebSafeString(cursorString);
                query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
            }

            if (limit != null) {
                query.setFirstResult(0);
                query.setMaxResults(limit);
            }

            execute = (List<UserTimer>) query.getResultList();
            cursor = JPACursorHelper.getCursor(execute);
            if (cursor != null) cursorString = cursor.toWebSafeString();

            // Tight loop for fetching all entities from datastore and accomodate
            // for lazy fetch.
            for (UserTimer obj : execute) ;
        } finally {
            if (mgr != null) {
                mgr.close();
            }
        }

        return CollectionResponse.<UserTimer>builder()
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
    @ApiMethod(name = "getUserTimer")
    public UserTimer getUserTimer(@Named("id") int id) {
        EntityManager mgr = getEntityManager();
        UserTimer userTimer = null;
        try {
            userTimer = mgr.find(UserTimer.class, id);
        } finally {
            mgr.close();
        }
        return userTimer;
    }

    /**
     * This inserts a new entity into App Engine datastore. If the entity already
     * exists in the datastore, an exception is thrown.
     * It uses HTTP POST method.
     *
     * @param userTimer the entity to be inserted.
     * @return The inserted entity.
     */
    @ApiMethod(name = "insertUserTimer")
    public UserTimer insertUserTimer(UserTimer userTimer) {
        EntityManager mgr = getEntityManager();
        try {
            if (containsUserTimer(userTimer)) {
                throw new EntityExistsException("Object already exists");
            }
            mgr.persist(userTimer);
        } finally {
            mgr.close();
        }
        return userTimer;
    }

    /**
     * This method is used for updating an existing entity. If the entity does not
     * exist in the datastore, an exception is thrown.
     * It uses HTTP PUT method.
     *
     * @param userTimer the entity to be updated.
     * @return The updated entity.
     */
    @ApiMethod(name = "updateUserTimer")
    public UserTimer updateUserTimer(UserTimer userTimer) {
        EntityManager mgr = getEntityManager();
        try {
            if (!containsUserTimer(userTimer)) {
                throw new EntityNotFoundException("Object does not exist");
            }
            mgr.persist(userTimer);
        } finally {
            mgr.close();
        }
        return userTimer;
    }

    /**
     * This method removes the entity with primary key id.
     * It uses HTTP DELETE method.
     *
     * @param id the primary key of the entity to be deleted.
     * @return The deleted entity.
     */
    @ApiMethod(name = "removeUserTimer")
    public UserTimer removeUserTimer(@Named("id") int id) {
        EntityManager mgr = getEntityManager();
        UserTimer userTimer = null;
        try {
            userTimer = mgr.find(UserTimer.class, id);
            mgr.remove(userTimer);
        } finally {
            mgr.close();
        }
        return userTimer;
    }

    private boolean containsUserTimer(UserTimer userTimer) {
        EntityManager mgr = getEntityManager();
        boolean contains = true;
        try {
            UserTimer item = mgr.find(UserTimer.class, userTimer.getPairId());
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
