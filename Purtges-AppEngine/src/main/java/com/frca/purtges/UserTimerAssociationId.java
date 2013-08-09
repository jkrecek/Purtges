package com.frca.purtges;

import java.io.Serializable;

public class UserTimerAssociationId implements Serializable {
    public long userId;

    public long timerId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getTimerId() {
        return timerId;
    }

    public void setTimerId(long timerId) {
        this.timerId = timerId;
    }

    @Override
    public int hashCode() {
        return (int)(userId + timerId);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof UserTimerAssociationId) {
            UserTimerAssociationId otherId = (UserTimerAssociationId) object;
            return (otherId.userId == this.userId) && (otherId.timerId == this.timerId);
        }
        return false;
    }

    @Override
    public String toString() {
        return userId + ":" + "timerId";
    }

}
