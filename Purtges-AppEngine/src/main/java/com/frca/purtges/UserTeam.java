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
public class UserTeam {

    @Id
    private int pairId;

    @OneToOne
    @MapsId
    private UserData userData;

    @OneToOne
    @MapsId
    private Team team;

    @Enumerated(EnumType.ORDINAL)
    private Values.Role role;

    public int getPairId() {
        return pairId;
    }

    public void setPairId(int pairId) {
        this.pairId = pairId;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Values.Role getRole() {
        return role;
    }

    public void setRole(Values.Role role) {
        this.role = role;
    }
}
