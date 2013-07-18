package com.frca.purtges;

import com.frca.purtges.Const.Ids;
import com.frca.purtges.helpers.Values;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;
import com.google.appengine.datanucleus.query.JPACursorHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Api(
    name = "userteamendpoint",
    namespace = @ApiNamespace(ownerDomain = "frca.com", ownerName = "frca.com", packagePath = Ids.PACKAGE_NAME),
    clientIds = {Ids.WEB_CLIENT_ID, Ids.ANDROID_CLIENT_ID},
    audiences = {Ids.ANDROID_AUDIENCE}
)

public class UserTeamEndpoint {

    @SuppressWarnings({"unchecked", "unused"})
    @ApiMethod(name = "listUserTeam")
    public CollectionResponse<UserTeam> listUserTeam(
        @Nullable @Named("cursor") String cursorString,
        @Nullable @Named("limit") Integer limit) {

        EntityManager mgr = null;
        List<UserTeam> execute = null;

        try {
            mgr = getEntityManager();
            Query query = mgr.createQuery("select from UserTeam as UserTeam");
            Cursor cursor;
            if (cursorString != null && cursorString.trim().length() > 0) {
                cursor = Cursor.fromWebSafeString(cursorString);
                query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
            }

            if (limit != null) {
                query.setFirstResult(0);
                query.setMaxResults(limit);
            }

            execute = (List<UserTeam>) query.getResultList();
            cursor = JPACursorHelper.getCursor(execute);
            if (cursor != null) cursorString = cursor.toWebSafeString();

            // Tight loop for fetching all entities from datastore and accomodate
            // for lazy fetch.
            for (UserTeam obj : execute) ;
        } finally {
            if (mgr != null) {
                mgr.close();
            }
        }

        return CollectionResponse.<UserTeam>builder()
            .setItems(execute)
            .setNextPageToken(cursorString)
            .build();
    }

    @ApiMethod(name = "getUserTeam")
    public UserTeam getUserTeam(@Named("id") int id) {
        EntityManager mgr = getEntityManager();
        UserTeam userTeam = null;
        try {
            userTeam = mgr.find(UserTeam.class, id);
        } finally {
            mgr.close();
        }
        return userTeam;
    }

    @ApiMethod(name = "insertUserTeam")
    public UserTeam insertUserTeam(
        @Named("name") String name,
        @Named("passwordHash") String passwordHash,
        User user)
        throws OAuthRequestException, IOException {

        EntityManager mgr = getEntityManager();
        UserDataEndpoint userDataEndpoint = new UserDataEndpoint();
        UserData userData = userDataEndpoint.findUserData(user);

        UserTeam userTeam = null;
        try {

            TeamEndpoint teamEndpoint = new TeamEndpoint();

            Team team = teamEndpoint.findTeamByName(name);
            boolean create = team == null;

            if (create) {
                team = new Team();
                team.setName(name);
                team.setPasswordHash(passwordHash);
                team.setGameType(1); // TODO
                team.setCurrentTimer(null);
                team = teamEndpoint.insertTeam(team);
            }

            //if (containsUserTeam(userData, team))
            //    throw new UserDataEndpoint.NoUserException("User is already in the team");

            userTeam = new UserTeam();
            userTeam.setUserData(userData);
            userTeam.setTeam(team);
            userTeam.setRole(create ? Values.Role.ADMIN : Values.Role.MEMBER);
            mgr.persist(userTeam);
        } finally {
            mgr.close();
        }
        return userTeam;
    }

    public List<UserData> findUsersInTeam(Team team) {
        EntityManager mgr = getEntityManager();
        List<UserData> users = null;
        try {
            Query query = mgr.createQuery("select from UserTeam as UserTeam where team = :team");
            query.setParameter("team", team);

            List<UserTeam> response = query.getResultList();
            users = new ArrayList<UserData>();
            for (UserTeam userTeam : response)
                users.add(userTeam.getUserData());
        } finally {
            mgr.close();
        }
        return users;
    }

    public List<Team> findTeamsOfUser(UserData userData) {
        EntityManager mgr = getEntityManager();
        List<Team> teams = null;
        try {
            Query query = mgr.createQuery("select from UserTeam as UserTeam where userData = :userData");
            query.setParameter("userData", userData);

            List<UserTeam> response = query.getResultList();
            teams = new ArrayList<Team>();
            for (UserTeam userTeam : response)
                teams.add(userTeam.getTeam());
        } finally {
            mgr.close();
        }
        return teams;
    }

    private boolean containsUserTeam(UserData userData, Team team) {
        EntityManager mgr = getEntityManager();
        boolean contains = true;
        try {
            Query query = mgr.createQuery("select count(1) from UserTeam as UserTeam where userData = :userData and team = :team");
            query.setParameter("userData", userData);
            query.setParameter("team", team);
            query.setFirstResult(0);
            query.setMaxResults(1);
            contains = !query.getResultList().isEmpty();
        } finally {
            mgr.close();
        }
        return contains;
    }

    /*@ApiMethod(name = "updateUserTeam")
    public UserTeam updateUserTeam(UserTeam userTeam) {
        EntityManager mgr = getEntityManager();
        try {
            if (!containsUserTeam(userTeam)) {
                throw new EntityNotFoundException("Object does not exist");
            }
            mgr.persist(userTeam);
        } finally {
            mgr.close();
        }
        return userTeam;
    }*/

    /*@ApiMethod(name = "removeUserTeam")
    public UserTeam removeUserTeam(@Named("id") int id) {
        EntityManager mgr = getEntityManager();
        UserTeam userTeam = null;
        try {
            userTeam = mgr.find(UserTeam.class, id);
            mgr.remove(userTeam);
        } finally {
            mgr.close();
        }
        return userTeam;
    }*/

    private static EntityManager getEntityManager() {
        return EMF.get().createEntityManager();
    }

}
