package com.vinsasoft.security.CallBlocker;


public class TooShortNumberException extends Exception {
    public static final int MINIMUM_LENGTH = 4;

    public TooShortNumberException(String message) {
        super(message);
    }
}
