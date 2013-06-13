package com.frca.gamingscheduler;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserDevice {

    @Id
    private String deviceRegID;

    private String userEmail;

    public String getDeviceRegID() {
        return deviceRegID;
    }

    public void setDeviceRegID(String deviceRegID) {
        this.deviceRegID = deviceRegID;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
