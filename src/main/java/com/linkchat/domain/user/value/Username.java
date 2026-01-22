package com.linkchat.domain.user.value;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Username {
    private final String value;

    private Username(String value) {
        if (value == null || value.isEmpty() || value.length() < 3 || value.length() > 20) {
            throw new IllegalArgumentException("Username must be between 3 and 20 characters");
        }
        this.value = value;
    }

    public static Username of(String value) {
        return new Username(value);
    }

    @Override
    public String toString() {
        return value;
    }
}