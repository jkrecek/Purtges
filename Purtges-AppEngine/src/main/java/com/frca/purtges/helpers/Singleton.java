package com.frca.purtges.helpers;

/**
 * Holder for singleton objects
 */
public class Singleton {

    private static GCM gcm;

    public static GCM GCM() {
        if (gcm == null)
            gcm = new GCM();

        return gcm;
    }
}
