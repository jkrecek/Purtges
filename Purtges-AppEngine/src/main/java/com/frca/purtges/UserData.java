package com.frca.purtges;

import com.google.appengine.api.datastore.Key;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class UserData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key id;

    private String email;

    private String displayName;

    @Basic
    private List<String> deviceIds;// = new ArrayList<String>();

    @ManyToMany
    private List<TeamData> teams;// = new ArrayList<TeamData>();

    @OneToMany(mappedBy="userData")
    private List<UserTimerAssociation> timers;// = new ArrayList<TeamTimer>();

    public Key getId() {
        return id;
    }

    public void setId(Key id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<String> getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(List<String> deviceIds) {
        this.deviceIds = deviceIds;
    }

    public List<TeamData> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamData> teams) {
        this.teams = teams;
    }

    public List<UserTimerAssociation> getTimers() {
        return timers;
    }

    public void setTimers(List<UserTimerAssociation> timers) {
        this.timers = timers;
    }
}
