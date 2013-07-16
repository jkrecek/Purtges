package com.frca.purtges;

import com.frca.purtges.Const.Ids;
import com.frca.purtges.helpers.Values;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.users.User;
import com.google.appengine.datanucleus.query.JPACursorHelper;

import java.util.List;
import java.util.logging.Level;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Api(
    name = "userdataendpoint",
    namespace = @ApiNamespace(ownerDomain = "frca.com", ownerName = "frca.com", packagePath = Ids.PACKAGE_NAME),
    clientIds = {Ids.WEB_CLIENT_ID, Ids.ANDROID_CLIENT_ID},
    audiences = {Ids.ANDROID_AUDIENCE}
)
public class UserDataEndpoint {

    @SuppressWarnings({"unchecked", "unused"})
    @ApiMethod(name = "listUserData")
    public CollectionResponse<UserData> listUserData(
        @Nullable @Named("cursor") String cursorString,
        @Nullable @Named("limit") Integer limit) {

        EntityManager mgr = null;
        List<UserData> execute = null;

        try {
            mgr = getEntityManager();
            Query query = mgr.createQuery("select from UserData as UserData");
            Cursor cursor;
            if (cursorString != null && cursorString.trim().length() > 0) {
                cursor = Cursor.fromWebSafeString(cursorString);
                query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
            }

            if (limit != null) {
                query.setFirstResult(0);
                query.setMaxResults(limit);
            }

            execute = (List<UserData>) query.getResultList();
            cursor = JPACursorHelper.getCursor(execute);
            if (cursor != null) cursorString = cursor.toWebSafeString();

            // Tight loop for fetching all entities from datastore and accomodate
            // for lazy fetch.
            for (UserData obj : execute) ;
        } finally {
            if (mgr != null) {
                mgr.close();
            }
        }

        return CollectionResponse.<UserData>builder()
            .setItems(execute)
            .setNextPageToken(cursorString)
            .build();
    }

    @ApiMethod(name = "getUserData")
    public UserData getUserData(@Named("id") String id) {
        EntityManager mgr = getEntityManager();
        UserData userData = null;
        try {
            if (id != null)
                userData = mgr.find(UserData.class, id);
        } finally {
            mgr.close();
        }
        return userData;
    }

    @ApiMethod(name = "insertUserData")
    public UserData insertUserData(@Named("displayName") String displayName, User user) {
        EntityManager mgr = getEntityManager();
        UserData userData = null;
        try {
            // TODO: Check existing displayName?
            /*if (containsUserData(userData)) {
                throw new EntityExistsException("Object already exists");
            }*/
            userData = new UserData();
            userData.setEmail(user.getEmail());
            userData.setDisplayName(displayName);
            mgr.persist(userData);
        } finally {
            mgr.close();
        }
        return userData;
    }

    private boolean containsUserData(UserData userData) {
        EntityManager mgr = getEntityManager();
        boolean contains = true;
        try {
            UserData item = mgr.find(UserData.class, userData.getEmail());
            if (item == null) {
                contains = false;
            }
        } finally {
            mgr.close();
        }
        return contains;
    }

    public UserData findUserData(User user) {
        if (user == null)
            return null;

        EntityManager mgr = getEntityManager();
        UserData userData = null;
        try {
            userData = mgr.find(UserData.class, user.getEmail());
        } finally {
            mgr.close();
        }

        return userData;
    }

    private static EntityManager getEntityManager() {
        return EMF.get().createEntityManager();
    }

}
