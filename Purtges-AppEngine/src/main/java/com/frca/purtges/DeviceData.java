package com.frca.purtges;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DeviceData {

    @Id
    private String registrationId;

    private String owner;

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
