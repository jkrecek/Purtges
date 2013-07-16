package com.frca.purtges;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;

@Entity
@TableGenerator(name="team", initialValue=0)
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "team")
    private int id;

    private String name;

    private String passwordHash;

    private int gameType;

    @OneToOne
    @MapsId
    private TeamTimer currentTimer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public TeamTimer getCurrentTimer() {
        return currentTimer;
    }

    public void setCurrentTimer(TeamTimer currentTimer) {
        this.currentTimer = currentTimer;
    }
}
