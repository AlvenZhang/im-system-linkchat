package com.linkchat.domain.user.value;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "test@example.com",
            "user.name@example.co.uk",
            "user+tag@example.com",
            "user_name@example.com"
    })
    void should_CreateEmail_When_ValidEmailFormat(String validEmail) {
        // When
        Email email = Email.of(validEmail);
        
        // Then
        assertNotNull(email);
        assertEquals(validEmail, email.getValue());
        assertEquals(validEmail, email.toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "invalid-email",
            "invalid@",
            "@invalid.com",
            "invalid@.com",
            "invalid.com"
    })
    void should_ThrowException_When_InvalidEmailFormat(String invalidEmail) {
        // Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Email.of(invalidEmail);
        });
        assertEquals("Invalid email format", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_NullEmail() {
        // Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Email.of(null);
        });
        assertEquals("Invalid email format", exception.getMessage());
    }

    @Test
    void should_BeEqual_When_SameEmailValue() {
        // Given
        Email email1 = Email.of("test@example.com");
        Email email2 = Email.of("test@example.com");
        
        // Then
        assertEquals(email1, email2);
        assertEquals(email1.hashCode(), email2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentEmailValue() {
        // Given
        Email email1 = Email.of("test1@example.com");
        Email email2 = Email.of("test2@example.com");
        
        // Then
        assertNotEquals(email1, email2);
        assertNotEquals(email1.hashCode(), email2.hashCode());
    }
}
