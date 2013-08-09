package com.frca.purtges.helpers;

import java.io.IOException;

public class NoKeyException extends IOException {
    public NoKeyException() {
        super("No Key Set");
    }
}
