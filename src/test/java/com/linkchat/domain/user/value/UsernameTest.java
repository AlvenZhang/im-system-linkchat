package com.linkchat.domain.user.value;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class UsernameTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "user",
            "validusername",
            "12345",
            "user_123",
            "a1b2c3d4e5f6g7h8i9j"
    })
    void should_CreateUsername_When_ValidUsernameLength(String validUsername) {
        // When
        Username username = Username.of(validUsername);
        
        // Then
        assertNotNull(username);
        assertEquals(validUsername, username.getValue());
        assertEquals(validUsername, username.toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "ab",
            "12",
            "a",
            ""
    })
    void should_ThrowException_When_UsernameTooShort(String invalidUsername) {
        // Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Username.of(invalidUsername);
        });
        assertEquals("Username must be between 3 and 20 characters", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_UsernameTooLong() {
        // Given
        String tooLongUsername = "a1b2c3d4e5f6g7h8i9j0k"; // 21 characters
        
        // Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Username.of(tooLongUsername);
        });
        assertEquals("Username must be between 3 and 20 characters", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_NullUsername() {
        // Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Username.of(null);
        });
        assertEquals("Username must be between 3 and 20 characters", exception.getMessage());
    }

    @Test
    void should_BeEqual_When_SameUsernameValue() {
        // Given
        Username username1 = Username.of("testuser");
        Username username2 = Username.of("testuser");
        
        // Then
        assertEquals(username1, username2);
        assertEquals(username1.hashCode(), username2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentUsernameValue() {
        // Given
        Username username1 = Username.of("testuser1");
        Username username2 = Username.of("testuser2");
        
        // Then
        assertNotEquals(username1, username2);
        assertNotEquals(username1.hashCode(), username2.hashCode());
    }
}
