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
    name = "teammessagesendpoint",
    namespace = @ApiNamespace(ownerDomain = "frca.com", ownerName = "frca.com", packagePath = Ids.PACKAGE_NAME),
    clientIds = {Ids.WEB_CLIENT_ID, Ids.ANDROID_CLIENT_ID},
    audiences = {Ids.ANDROID_AUDIENCE}
)
public class TeamMessagesEndpoint {

    @SuppressWarnings({"unchecked", "unused"})
    @ApiMethod(name = "listTeamMessages")
    public CollectionResponse<TeamMessages> listTeamMessages(
        @Nullable @Named("cursor") String cursorString,
        @Nullable @Named("limit") Integer limit) {

        EntityManager mgr = null;
        List<TeamMessages> execute = null;

        try {
            mgr = getEntityManager();
            Query query = mgr.createQuery("select from TeamMessages as TeamMessages");
            Cursor cursor;
            if (cursorString != null && cursorString.trim().length() > 0) {
                cursor = Cursor.fromWebSafeString(cursorString);
                query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
            }

            if (limit != null) {
                query.setFirstResult(0);
                query.setMaxResults(limit);
            }

            execute = (List<TeamMessages>) query.getResultList();
            cursor = JPACursorHelper.getCursor(execute);
            if (cursor != null) cursorString = cursor.toWebSafeString();

            // Tight loop for fetching all entities from datastore and accomodate
            // for lazy fetch.
            for (TeamMessages obj : execute) ;
        } finally {
            if (mgr != null) {
                mgr.close();
            }
        }

        return CollectionResponse.<TeamMessages>builder()
            .setItems(execute)
            .setNextPageToken(cursorString)
            .build();
    }

    @ApiMethod(name = "getTeamMessages")
    public TeamMessages getTeamMessages(@Named("id") int id) {
        EntityManager mgr = getEntityManager();
        TeamMessages teamMessages = null;
        try {
            teamMessages = mgr.find(TeamMessages.class, id);
        } finally {
            mgr.close();
        }
        return teamMessages;
    }

    @ApiMethod(name = "insertTeamMessages")
    public TeamMessages insertTeamMessages(TeamMessages teamMessages) {
        EntityManager mgr = getEntityManager();
        try {
            if (containsTeamMessages(teamMessages)) {
                throw new EntityExistsException("Object already exists");
            }
            mgr.persist(teamMessages);
        } finally {
            mgr.close();
        }
        return teamMessages;
    }

    @ApiMethod(name = "updateTeamMessages")
    public TeamMessages updateTeamMessages(TeamMessages teamMessages) {
        EntityManager mgr = getEntityManager();
        try {
            if (!containsTeamMessages(teamMessages)) {
                throw new EntityNotFoundException("Object does not exist");
            }
            mgr.persist(teamMessages);
        } finally {
            mgr.close();
        }
        return teamMessages;
    }

    @ApiMethod(name = "removeTeamMessages")
    public TeamMessages removeTeamMessages(@Named("id") int id) {
        EntityManager mgr = getEntityManager();
        TeamMessages teamMessages = null;
        try {
            teamMessages = mgr.find(TeamMessages.class, id);
            mgr.remove(teamMessages);
        } finally {
            mgr.close();
        }
        return teamMessages;
    }

    private boolean containsTeamMessages(TeamMessages teamMessages) {
        EntityManager mgr = getEntityManager();
        boolean contains = true;
        try {
            TeamMessages item = mgr.find(TeamMessages.class, teamMessages.getId());
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
