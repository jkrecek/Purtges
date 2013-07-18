package com.frca.purtges.requests;

import com.frca.purtges.CloudEndpointUtils;
import com.frca.purtges.teamendpoint.Teamendpoint;
import com.frca.purtges.teammessagesendpoint.Teammessagesendpoint;
import com.frca.purtges.teamtimerendpoint.Teamtimerendpoint;
import com.frca.purtges.userdataendpoint.Userdataendpoint;
import com.frca.purtges.userteamendpoint.Userteamendpoint;
import com.frca.purtges.usertimerendpoint.Usertimerendpoint;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.jackson.JacksonFactory;

/**
 * Created by Frca on 11.7.13.
 */
public class EndpointHolder {

    private GoogleAccountCredential credential = null;

    public Teamendpoint teamendpoint = null;
    public Teammessagesendpoint teammessagesendpoint = null;
    public Teamtimerendpoint teamtimerendpoint = null;
    public Userdataendpoint userdataendpoint = null;
    public Userteamendpoint userteamendpoint = null;
    public Usertimerendpoint usertimerendpoint = null;

    public EndpointHolder(GoogleAccountCredential credential) {
        this.credential = credential;
    }

    public Teamendpoint team() {
        if (teamendpoint == null) {
            teamendpoint = CloudEndpointUtils.updateBuilder(
                    new Teamendpoint.Builder(
                            AndroidHttp.newCompatibleTransport(), new JacksonFactory(), credential
                    )
            ).build();
        }

        return teamendpoint;
    }

    public Teammessagesendpoint teamMessages() {
        if (teammessagesendpoint == null) {
            teammessagesendpoint = CloudEndpointUtils.updateBuilder(
                    new Teammessagesendpoint.Builder(
                            AndroidHttp.newCompatibleTransport(), new JacksonFactory(), credential
                    )
            ).build();
        }

        return teammessagesendpoint;
    }

    public Teamtimerendpoint teamTimer() {
        if (teamtimerendpoint == null) {
            teamtimerendpoint = CloudEndpointUtils.updateBuilder(
                    new Teamtimerendpoint.Builder(
                            AndroidHttp.newCompatibleTransport(), new JacksonFactory(), credential
                    )
            ).build();
        }

        return teamtimerendpoint;
    }

    public Userdataendpoint userData() {
        if (userdataendpoint == null) {
            userdataendpoint = CloudEndpointUtils.updateBuilder(
                    new Userdataendpoint.Builder(
                            AndroidHttp.newCompatibleTransport(), new JacksonFactory(), credential
                    )
            ).build();
        }

        return userdataendpoint;
    }

    public Userteamendpoint userTeam() {
        if (userteamendpoint == null) {
            userteamendpoint = CloudEndpointUtils.updateBuilder(
                    new Userteamendpoint.Builder(
                            AndroidHttp.newCompatibleTransport(), new JacksonFactory(), credential
                    )
            ).build();
        }

        return userteamendpoint;
    }

    public Usertimerendpoint userTimer() {
        if (usertimerendpoint == null) {
            usertimerendpoint = CloudEndpointUtils.updateBuilder(
                    new Usertimerendpoint.Builder(
                            AndroidHttp.newCompatibleTransport(), new JacksonFactory(), credential
                    )
            ).build();
        }

        return usertimerendpoint;
    }
}
