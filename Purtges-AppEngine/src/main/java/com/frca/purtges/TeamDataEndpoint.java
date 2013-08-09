package com.frca.purtges;

import com.frca.purtges.Const.Ids;
import com.frca.purtges.helpers.Method;
import com.frca.purtges.helpers.Values;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.Key;

import java.io.IOException;
import java.util.logging.Level;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

@Api(
    name = "teamdataendpoint",
    namespace = @ApiNamespace(ownerDomain = "frca.com", ownerName = "frca.com", packagePath = Ids.PACKAGE_NAME),
    clientIds = {Ids.WEB_CLIENT_ID, Ids.ANDROID_CLIENT_ID},
    audiences = {Ids.ANDROID_AUDIENCE}
)
public class TeamDataEndpoint {

    @ApiMethod(name = "getTeamData")
    public TeamData getTeamData(Key id) throws IOException {
        EntityManager mgr = getEntityManager();
        TeamData teamData = null;
        try {
            id = Method.checkKey(id);
            teamData = mgr.find(TeamData.class, id);
        } finally {
            mgr.close();
        }
        return teamData;
    }

    @ApiMethod(name = "insertTeamData")
    public TeamData insertTeamData(TeamData teamData) throws IOException  {
        EntityManager mgr = getEntityManager();
        try {
            Values.log.log(Level.SEVERE, "PUSHING");
            teamData = Method.checkKeyEntity(teamData);
            Values.log.log(Level.SEVERE, "CONTINUING");
            if (containsTeamData(teamData)) {
                throw new EntityExistsException("Object already exists");
            }
            mgr.persist(teamData);
        } finally {
            mgr.close();
        }
        return teamData;
    }

    @ApiMethod(name = "updateTeamData")
    public TeamData updateTeamData(TeamData teamData) throws IOException {
        EntityManager mgr = getEntityManager();
        try {
            teamData = Method.checkKeyEntity(teamData);
            if (!containsTeamData(teamData)) {
                throw new EntityNotFoundException("Object does not exist");
            }
            mgr.persist(teamData);
        } finally {
            mgr.close();
        }
        return teamData;
    }

    @ApiMethod(name = "removeTeamData")
    public TeamData removeTeamData(Key id) throws IOException {
        EntityManager mgr = getEntityManager();
        TeamData teamData = null;
        try {
            id = Method.checkKey(id);
            teamData = mgr.find(TeamData.class, id);
            mgr.remove(teamData);
        } finally {
            mgr.close();
        }
        return teamData;
    }

    private boolean containsTeamData(TeamData teamData) throws IOException {
        EntityManager mgr = getEntityManager();
        boolean contains = true;
        try {
            TeamData item = mgr.find(TeamData.class, teamData.getId());
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
