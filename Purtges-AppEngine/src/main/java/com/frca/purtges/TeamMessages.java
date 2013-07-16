package com.frca.purtges;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

/**
 * Created by KillerFrca on 9.7.13.
 */
@Entity
public class TeamMessages {
    @Id
    private int id;

    @OneToOne
    @MapsId
    private Team team;

    @OneToOne
    @MapsId
    private UserData author;

    private Date time;

    private String message;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public UserData getAuthor() {
        return author;
    }

    public void setAuthor(UserData author) {
        this.author = author;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getEssage() {
        return message;
    }

    public void setEssage(String essage) {
        message = essage;
    }
}
