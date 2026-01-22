package com.linkchat.domain.user.value;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class UserIdTest {

    @ParameterizedTest
    @ValueSource(longs = {
            1L,
            100L,
            999999L,
            Long.MAX_VALUE
    })
    void should_CreateUserId_When_ValidUserId(Long validUserId) {
        // When
        UserId userId = UserId.of(validUserId);
        
        // Then
        assertNotNull(userId);
        assertEquals(validUserId, userId.getValue());
        assertEquals(validUserId.toString(), userId.toString());
    }

    @Test
    void should_ThrowException_When_NullUserId() {
        // Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            UserId.of(null);
        });
        assertEquals("User ID must be positive", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(longs = {
            0L,
            -1L,
            -100L,
            Long.MIN_VALUE
    })
    void should_ThrowException_When_NonPositiveUserId(Long invalidUserId) {
        // Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            UserId.of(invalidUserId);
        });
        assertEquals("User ID must be positive", exception.getMessage());
    }

    @Test
    void should_BeEqual_When_SameUserIdValue() {
        // Given
        UserId userId1 = UserId.of(123L);
        UserId userId2 = UserId.of(123L);
        
        // Then
        assertEquals(userId1, userId2);
        assertEquals(userId1.hashCode(), userId2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentUserIdValue() {
        // Given
        UserId userId1 = UserId.of(123L);
        UserId userId2 = UserId.of(456L);
        
        // Then
        assertNotEquals(userId1, userId2);
        assertNotEquals(userId1.hashCode(), userId2.hashCode());
    }
}
