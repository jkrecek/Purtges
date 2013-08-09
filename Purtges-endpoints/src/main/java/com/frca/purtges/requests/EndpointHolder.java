package com.frca.purtges.requests;

import com.frca.purtges.helpers.CloudEndpointUtils;
import com.frca.purtges.teamdataendpoint.Teamdataendpoint;
import com.frca.purtges.teamtimerendpoint.Teamtimerendpoint;
import com.frca.purtges.userdataendpoint.Userdataendpoint;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.jackson.JacksonFactory;

public class EndpointHolder {

    private GoogleAccountCredential credential = null;

    public Teamdataendpoint teamendpoint = null;
    public Teamtimerendpoint teamtimerendpoint = null;
    public Userdataendpoint userdataendpoint = null;

    public EndpointHolder(GoogleAccountCredential credential) {
        this.credential = credential;
    }

    public Teamdataendpoint teamData() {
        if (teamendpoint == null) {
            teamendpoint = CloudEndpointUtils.updateBuilder(
                    new Teamdataendpoint.Builder(
                            AndroidHttp.newCompatibleTransport(), new JacksonFactory(), credential
                    )
            ).build();
        }

        return teamendpoint;
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
}
