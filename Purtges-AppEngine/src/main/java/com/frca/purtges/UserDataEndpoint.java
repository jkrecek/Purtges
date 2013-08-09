package com.frca.purtges;

import com.frca.purtges.Const.Ids;
import com.frca.purtges.helpers.Method;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;

import java.io.IOException;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Api(
    name = "userdataendpoint",
    namespace = @ApiNamespace(ownerDomain = "frca.com", ownerName = "frca.com", packagePath = Ids.PACKAGE_NAME),
    clientIds = {Ids.WEB_CLIENT_ID, Ids.ANDROID_CLIENT_ID},
    audiences = {Ids.ANDROID_AUDIENCE}
)
public class UserDataEndpoint {

    @ApiMethod(name = "claimUserData")
    public UserData claimUserData(Key id, User user) throws OAuthRequestException, IOException {
        EntityManager mgr = getEntityManager();
        UserData userData = null;
        try {
            id = Method.checkKey(id);
            if (id.getId() == 0)
                userData = findUserData(user);
            else
                userData = mgr.find(UserData.class, id);

        } finally {
            mgr.close();
        }
        return userData;
    }

    @ApiMethod(name = "insertUserData")
    public UserData insertUserData(UserData userData, User user) throws OAuthRequestException, IOException {
        EntityManager mgr = getEntityManager();
        try {
            userData = Method.checkKeyEntity(userData);
            if (!userData.getEmail().equals(user.getEmail()))
                throw new OAuthRequestException("Wrong user");

            if (userData.getId() != null && containsUserData(userData)) {
                throw new EntityExistsException("Object already exists");
            }
            userData.setEmail(user.getEmail());
            mgr.persist(userData);
        } finally {
            mgr.close();
        }
        return userData;
    }

    @ApiMethod(name = "updateUserData")
    public UserData updateUserData(UserData userData, User user) throws OAuthRequestException, IOException {
        EntityManager mgr = getEntityManager();
        try {
            userData = Method.checkKeyEntity(userData);
            if (!userData.getEmail().equals(user.getEmail()))
                throw new OAuthRequestException("Wrong user");

            if (!containsUserData(userData)) {
                throw new EntityNotFoundException("Object does not exist");
            }
            mgr.persist(userData);
        } finally {
            mgr.close();
        }
        return userData;
    }

    @ApiMethod(name = "removeUserData")
    public UserData removeUserData(Key id) throws IOException {
        EntityManager mgr = getEntityManager();
        UserData userData = null;
        try {
            id = Method.checkKey(id);
            userData = mgr.find(UserData.class, id);
            mgr.remove(userData);
        } finally {
            mgr.close();
        }
        return userData;
    }

    private boolean containsUserData(UserData userData) {
        EntityManager mgr = getEntityManager();
        boolean contains = true;
        try {
            UserData item = mgr.find(UserData.class, userData.getId());
            if (item == null) {
                contains = false;
            }
        } finally {
            mgr.close();
        }
        return contains;
    }

    public UserData findUserData(User user) throws OAuthRequestException {
        if (user == null)
            throw new OAuthRequestException("No user authed!");

        EntityManager mgr = getEntityManager();
        UserData userData = null;
        try {
            final CriteriaBuilder cb = mgr.getCriteriaBuilder();
            final CriteriaQuery<UserData> query = cb.createQuery(UserData.class);
            final Root<UserData> root = query.from(UserData.class);
            query.where(cb.equal(root.<String>get("email"), user.getEmail()));
            userData = mgr.createQuery(query).getSingleResult();
        } catch(NoResultException e) {
            // ignore
        } finally {
            mgr.close();
        }

        return userData;
    }

    private static EntityManager getEntityManager() {
        return EMF.get().createEntityManager();
    }

}
