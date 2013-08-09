package com.frca.purtges;

import com.frca.purtges.helpers.Values;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="TIMER_USER")
@IdClass(UserTimerAssociationId.class)
public class UserTimerAssociation {

    @Id
    private long userId;
    @Id
    private long timerId;

    @Enumerated(EnumType.ORDINAL)
    private Values.State state;

    private String reason;

    private boolean isAuthor;

    private boolean isCancelledBy;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="USERID", referencedColumnName="ID")
    private UserData userData;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="TIMERID", referencedColumnName="ID")
    private TeamTimer teamTimer;

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

    public Values.State getState() {
        return state;
    }

    public void setState(Values.State state) {
        this.state = state;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public TeamTimer getTeamTimer() {
        return teamTimer;
    }

    public void setTeamTimer(TeamTimer teamTimer) {
        this.teamTimer = teamTimer;
    }

    public boolean isAuthor() {
        return isAuthor;
    }

    public void setAuthor(boolean author) {
        isAuthor = author;
    }

    public boolean isCancelledBy() {
        return isCancelledBy;
    }

    public void setCancelledBy(boolean cancelledBy) {
        isCancelledBy = cancelledBy;
    }
}
