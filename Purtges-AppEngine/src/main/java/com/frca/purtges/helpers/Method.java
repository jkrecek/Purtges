package com.frca.purtges.helpers;

import com.frca.purtges.EMF;
import com.google.appengine.api.datastore.Key;

public class Method {

    public static Key checkKey(Key key) throws NoKeyException {
        if (key == null)
            throw new NoKeyException();

        return key;
    }

    private static <B> B checkEntity(B entity, boolean checkKey) throws EntityException {
        if (entity == null)
            throw new EntityException("Entity is null");

        Object identifier = EMF.get().getPersistenceUnitUtil().getIdentifier(entity);
        if (identifier == null)
            throw new EntityException("Entity has no identifier");

        if (EMF.get().getPersistenceUnitUtil().getIdentifier(entity) instanceof Key)
            return entity;
        else
            throw new EntityException("Entity has wrong");
    }

    public static <B> B checkKeyEntity(B entity) throws EntityException {
        return checkEntity(entity, true);
    }

    public static <B> B checkNonkeyEntity(B entity) throws EntityException {
        return checkEntity(entity, false);
    }
}
