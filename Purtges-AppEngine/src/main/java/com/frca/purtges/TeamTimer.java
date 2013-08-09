package com.frca.purtges;

import com.frca.purtges.helpers.Values;
import com.google.appengine.api.datastore.Key;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;

/**
 * Created by KillerFrca on 9.7.13.
 */
@Entity
@TableGenerator(name="timer", initialValue=0)

public class TeamTimer {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "timer")
    private Key id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="TEAM_ID")
    private TeamData teamData;

    private Date time;

    private String cancelReason;

    @OneToMany(mappedBy="teamTimer")
    private List<UserTimerAssociation> users;

    public Key getId() {
        return id;
    }

    public void setId(Key id) {
        this.id = id;
    }

    public TeamData getTeamData() {
        return teamData;
    }

    public void setTeamData(TeamData teamData) {
        this.teamData = teamData;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public List<UserTimerAssociation> getUsers() {
        return users;
    }

    public void setUsers(List<UserTimerAssociation> users) {
        this.users = users;
    }

    public void adduser(UserData userData) {
        UserTimerAssociation association = new UserTimerAssociation();
        association.setUserData(userData);
        association.setTeamTimer(this);
        association.setUserId(userData.getId().getId());
        association.setTimerId(this.getId().getId());
        association.setState(Values.State.NOT_RESPONDED);
        association.setReason(null);

        this.getUsers().add(association);
        userData.getTimers().add(association);
    }
}
