package com.linkchat.domain.user.value;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;

@Getter
@EqualsAndHashCode
public class UserId {
    private final Long value;

    private UserId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
        this.value = value;
    }

    public static UserId of(Long value) {
        return new UserId(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}