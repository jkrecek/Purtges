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
    name = "teamtimerendpoint",
    namespace = @ApiNamespace(ownerDomain = "frca.com", ownerName = "frca.com", packagePath = Ids.PACKAGE_NAME),
    clientIds = {Ids.WEB_CLIENT_ID, Ids.ANDROID_CLIENT_ID},
    audiences = {Ids.ANDROID_AUDIENCE}
)
public class TeamTimerEndpoint {

    /**
     * This method lists all the entities inserted in datastore.
     * It uses HTTP GET method and paging support.
     *
     * @return A CollectionResponse class containing the list of all entities
     *         persisted and a cursor to the next page.
     */
    @SuppressWarnings({"unchecked", "unused"})
    @ApiMethod(name = "listTeamTimer")
    public CollectionResponse<TeamTimer> listTeamTimer(
        @Nullable @Named("cursor") String cursorString,
        @Nullable @Named("limit") Integer limit) {

        EntityManager mgr = null;
        List<TeamTimer> execute = null;

        try {
            mgr = getEntityManager();
            Query query = mgr.createQuery("select from TeamTimer as TeamTimer");
            Cursor cursor;
            if (cursorString != null && cursorString.trim().length() > 0) {
                cursor = Cursor.fromWebSafeString(cursorString);
                query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
            }

            if (limit != null) {
                query.setFirstResult(0);
                query.setMaxResults(limit);
            }

            execute = (List<TeamTimer>) query.getResultList();
            cursor = JPACursorHelper.getCursor(execute);
            if (cursor != null) cursorString = cursor.toWebSafeString();

            // Tight loop for fetching all entities from datastore and accomodate
            // for lazy fetch.
            for (TeamTimer obj : execute) ;
        } finally {
            if (mgr != null) {
                mgr.close();
            }
        }

        return CollectionResponse.<TeamTimer>builder()
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
    @ApiMethod(name = "getTeamTimer")
    public TeamTimer getTeamTimer(@Named("id") int id) {
        EntityManager mgr = getEntityManager();
        TeamTimer teamTimer = null;
        try {
            teamTimer = mgr.find(TeamTimer.class, id);
        } finally {
            mgr.close();
        }
        return teamTimer;
    }

    /**
     * This inserts a new entity into App Engine datastore. If the entity already
     * exists in the datastore, an exception is thrown.
     * It uses HTTP POST method.
     *
     * @param teamTimer the entity to be inserted.
     * @return The inserted entity.
     */
    @ApiMethod(name = "insertTeamTimer")
    public TeamTimer insertTeamTimer(TeamTimer teamTimer) {
        EntityManager mgr = getEntityManager();
        try {
            if (containsTeamTimer(teamTimer)) {
                throw new EntityExistsException("Object already exists");
            }
            mgr.persist(teamTimer);
        } finally {
            mgr.close();
        }
        return teamTimer;
    }

    /**
     * This method is used for updating an existing entity. If the entity does not
     * exist in the datastore, an exception is thrown.
     * It uses HTTP PUT method.
     *
     * @param teamTimer the entity to be updated.
     * @return The updated entity.
     */
    @ApiMethod(name = "updateTeamTimer")
    public TeamTimer updateTeamTimer(TeamTimer teamTimer) {
        EntityManager mgr = getEntityManager();
        try {
            if (!containsTeamTimer(teamTimer)) {
                throw new EntityNotFoundException("Object does not exist");
            }
            mgr.persist(teamTimer);
        } finally {
            mgr.close();
        }
        return teamTimer;
    }

    /**
     * This method removes the entity with primary key id.
     * It uses HTTP DELETE method.
     *
     * @param id the primary key of the entity to be deleted.
     * @return The deleted entity.
     */
    @ApiMethod(name = "removeTeamTimer")
    public TeamTimer removeTeamTimer(@Named("id") int id) {
        EntityManager mgr = getEntityManager();
        TeamTimer teamTimer = null;
        try {
            teamTimer = mgr.find(TeamTimer.class, id);
            mgr.remove(teamTimer);
        } finally {
            mgr.close();
        }
        return teamTimer;
    }

    private boolean containsTeamTimer(TeamTimer teamTimer) {
        EntityManager mgr = getEntityManager();
        boolean contains = true;
        try {
            TeamTimer item = mgr.find(TeamTimer.class, teamTimer.getId());
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
