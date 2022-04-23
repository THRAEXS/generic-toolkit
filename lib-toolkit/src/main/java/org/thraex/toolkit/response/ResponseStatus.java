package org.thraex.toolkit.response;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author 鬼王
 * @date 2022/03/16 13:26
 */
public enum ResponseStatus {

    OK(20000, "OK"),
    ILLEGAL_ARGUMENT(40000, "Illegal Argument"),
    TARGET_NOT_EXIST(40004, "Target does not exist"),
    AUTHENTICATION_UNAUTHORIZED(40401, "Unauthorized"),
    AUTHENTICATION_ACCESS_DENIED(40403, "Access Denied"),
    AUTHENTICATION_BAD_CREDENTIALS(40500, "Invalid Credentials"),
    AUTHENTICATION_INVALID_TOKEN(40501, "Invalid Token"),
    AUTHENTICATION_EXPIRED_TOKEN(40502, "Expired Token"),
    INTERNAL_SERVER_ERROR(50000, "Internal Server Error");

    private final int value;

    private final String phrase;

    ResponseStatus(int value, String phrase) {
        this.value = value;
        this.phrase = phrase;
    }

    public int value() {
        return value;
    }

    public String phrase() {
        return phrase;
    }

    public static Optional<ResponseStatus> of(int value) {
        return Stream.of(values()).filter(it -> it.value == value).findFirst();
    }

    @Override
    public String toString() {
        return String.format("%s %d %s", name(), value, phrase);
    }

}
