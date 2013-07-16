package com.frca.purtges.helpers;

import java.util.logging.Logger;

public class Values {
    public enum Role {
        ADMIN,
        MEMBER
    }

    public enum State {
        NOT_RESPONDED,
        READY,
        DECLINE
    }

    public static final Logger log = Logger.getLogger(Values.class.getName());
}
