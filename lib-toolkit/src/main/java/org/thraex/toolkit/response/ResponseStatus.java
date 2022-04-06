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
