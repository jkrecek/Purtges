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
import javax.persistence.NoResultException;
import javax.persistence.Query;

@Api(
    name = "teamendpoint",
    namespace = @ApiNamespace(ownerDomain = "frca.com", ownerName = "frca.com", packagePath = Ids.PACKAGE_NAME),
    clientIds = {Ids.WEB_CLIENT_ID, Ids.ANDROID_CLIENT_ID},
    audiences = {Ids.ANDROID_AUDIENCE}
)
public class TeamEndpoint {

    @SuppressWarnings({"unchecked", "unused"})
    @ApiMethod(name = "listTeam")
    public CollectionResponse<Team> listTeam(
        @Nullable @Named("cursor") String cursorString,
        @Nullable @Named("limit") Integer limit) {

        EntityManager mgr = null;
        List<Team> execute = null;

        try {
            mgr = getEntityManager();
            Query query = mgr.createQuery("select from Team as Team");
            Cursor cursor;
            if (cursorString != null && cursorString.trim().length() > 0) {
                cursor = Cursor.fromWebSafeString(cursorString);
                query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
            }

            if (limit != null) {
                query.setFirstResult(0);
                query.setMaxResults(limit);
            }

            execute = (List<Team>) query.getResultList();
            cursor = JPACursorHelper.getCursor(execute);
            if (cursor != null) cursorString = cursor.toWebSafeString();

            // Tight loop for fetching all entities from datastore and accomodate
            // for lazy fetch.
            for (Team obj : execute) ;
        } finally {
            if (mgr != null) {
                mgr.close();
            }
        }

        return CollectionResponse.<Team>builder()
            .setItems(execute)
            .setNextPageToken(cursorString)
            .build();
    }

    @ApiMethod(name = "getTeam")
    public Team getTeam(@Named("id") int id) {
        EntityManager mgr = getEntityManager();
        Team team = null;
        try {
            team = mgr.find(Team.class, id);
        } finally {
            mgr.close();
        }
        return team;
    }

    /*@ApiMethod(name = "getTeamByName")*/
    public Team findTeamByName(@Named("name") String name) throws NoResultException {
        EntityManager mgr = getEntityManager();
        Team team = null;
        try {
            Query query = mgr.createQuery("select from Team as Team where name = :name");
            query.setParameter("name", name);
            query.setFirstResult(0);
            query.setMaxResults(1);
            team = (Team)query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            mgr.close();
        }
        return team;
    }

    @ApiMethod(name = "insertTeam")
    public Team insertTeam(Team team) {
        EntityManager mgr = getEntityManager();
        try {
            if (containsTeam(team)) {
                throw new EntityExistsException("Object already exists");
            }
            mgr.persist(team);
        } finally {
            mgr.close();
        }
        return team;
    }

    /**
     * This method is used for updating an existing entity. If the entity does not
     * exist in the datastore, an exception is thrown.
     * It uses HTTP PUT method.
     *
     * @param team the entity to be updated.
     * @return The updated entity.
     */
    @ApiMethod(name = "updateTeam")
    public Team updateTeam(Team team) {
        EntityManager mgr = getEntityManager();
        try {
            if (!containsTeam(team)) {
                throw new EntityNotFoundException("Object does not exist");
            }
            mgr.persist(team);
        } finally {
            mgr.close();
        }
        return team;
    }

    /**
     * This method removes the entity with primary key id.
     * It uses HTTP DELETE method.
     *
     * @param id the primary key of the entity to be deleted.
     * @return The deleted entity.
     */
    @ApiMethod(name = "removeTeam")
    public Team removeTeam(@Named("id") int id) {
        EntityManager mgr = getEntityManager();
        Team team = null;
        try {
            team = mgr.find(Team.class, id);
            mgr.remove(team);
        } finally {
            mgr.close();
        }
        return team;
    }

    private boolean containsTeam(Team team) {
        EntityManager mgr = getEntityManager();
        boolean contains = true;
        try {
            Team item = mgr.find(Team.class, team.getId());
            if (item == null) {
                contains = false;
            }
        } finally {
            mgr.close();
        }
        return contains;
    }

    /*private boolean isPasswordCorrect()*/

    private static EntityManager getEntityManager() {
        return EMF.get().createEntityManager();
    }

}
