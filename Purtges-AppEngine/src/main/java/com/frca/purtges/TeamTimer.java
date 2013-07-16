package com.frca.purtges;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;

/**
 * Created by KillerFrca on 9.7.13.
 */
@Entity
@TableGenerator(name="timer", initialValue=0)

public class TeamTimer {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "timer")
    private int id;

    @OneToOne
    @MapsId
    private Team team;

    private Date time;

    @OneToOne
    @MapsId
    private UserData author;

    private String cancelReason;

    @OneToOne
    @MapsId
    private UserData cancelAuthor;

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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public UserData getAuthor() {
        return author;
    }

    public void setAuthor(UserData author) {
        this.author = author;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public UserData getCancelAuthor() {
        return cancelAuthor;
    }

    public void setCancelAuthor(UserData cancelAuthor) {
        this.cancelAuthor = cancelAuthor;
    }
}
