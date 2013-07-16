package com.frca.purtges;

import com.frca.purtges.helpers.Values;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

/**
 * Created by KillerFrca on 9.7.13.
 */
@Entity
public class UserTimer {

    @Id
    private int pairId;

    @OneToOne
    @MapsId
    private TeamTimer timer;

    @OneToOne
    @MapsId
    private UserData mUserData;

    @Enumerated(EnumType.ORDINAL)
    private Values.State state;

    private String reason;

    public int getPairId() {
        return pairId;
    }

    public void setPairId(int pairId) {
        this.pairId = pairId;
    }

    public TeamTimer getTimer() {
        return timer;
    }

    public void setTimer(TeamTimer timer) {
        this.timer = timer;
    }

    public UserData getUserData() {
        return mUserData;
    }

    public void setUserData(UserData userData) {
        this.mUserData = userData;
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
}
