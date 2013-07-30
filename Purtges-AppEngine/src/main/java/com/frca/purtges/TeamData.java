package com.frca.purtges;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;

@Entity
public class TeamData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String passwordHash;

    private int gameType;

    @ManyToMany
    private List<TeamTimer> timers = new ArrayList<TeamTimer>();

    @ManyToMany
    private List<UserData> users = new ArrayList<UserData>();

    private UserData admin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public int getGameType() {
        return gameType;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
    }

    public List<TeamTimer> getTimers() {
        return timers;
    }

    public void setTimers(List<TeamTimer> timers) {
        this.timers = timers;
    }

    public List<UserData> getUsers() {
        return users;
    }

    public void setUsers(List<UserData> users) {
        this.users = users;
    }

    public UserData getAdmin() {
        return admin;
    }

    public void setAdmin(UserData admin) {
        this.admin = admin;
    }
}
