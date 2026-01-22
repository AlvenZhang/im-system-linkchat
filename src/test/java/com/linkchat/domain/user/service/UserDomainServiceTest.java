package com.linkchat.domain.user.service;

import com.linkchat.domain.user.entity.User;
import com.linkchat.domain.user.repository.UserRepository;
import com.linkchat.domain.user.value.Email;
import com.linkchat.domain.user.value.Username;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDomainServiceTest {

    private UserRepository userRepository;
    private UserDomainService userDomainService;

    @BeforeEach
    void setUp() {
        // 创建模拟对象
        userRepository = Mockito.mock(UserRepository.class);
        // 初始化测试对象
        userDomainService = new UserDomainService(userRepository);
    }

    @Test
    void should_ReturnTrue_When_UsernameAvailable() {
        // Given
        String username = "newuser";
        when(userRepository.findByUsername(any(Username.class))).thenReturn(null);
        
        // When
        boolean result = userDomainService.isUsernameAvailable(username);
        
        // Then
        assertTrue(result);
        verify(userRepository, times(1)).findByUsername(any(Username.class));
    }

    @Test
    void should_ReturnFalse_When_UsernameNotAvailable() {
        // Given
        String username = "existinguser";
        User existingUser = new User.Builder()
                .id(1L)
                .username(username)
                .email("existing@example.com")
                .password("password")
                .nickname("Existing User")
                .status(1)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        when(userRepository.findByUsername(any(Username.class))).thenReturn(existingUser);
        
        // When
        boolean result = userDomainService.isUsernameAvailable(username);
        
        // Then
        assertFalse(result);
        verify(userRepository, times(1)).findByUsername(any(Username.class));
    }

    @Test
    void should_ReturnTrue_When_EmailAvailable() {
        // Given
        String email = "new@example.com";
        when(userRepository.findByEmail(any(Email.class))).thenReturn(null);
        
        // When
        boolean result = userDomainService.isEmailAvailable(email);
        
        // Then
        assertTrue(result);
        verify(userRepository, times(1)).findByEmail(any(Email.class));
    }

    @Test
    void should_ReturnFalse_When_EmailNotAvailable() {
        // Given
        String email = "existing@example.com";
        User existingUser = new User.Builder()
                .id(1L)
                .username("existinguser")
                .email(email)
                .password("password")
                .nickname("Existing User")
                .status(1)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        when(userRepository.findByEmail(any(Email.class))).thenReturn(existingUser);
        
        // When
        boolean result = userDomainService.isEmailAvailable(email);
        
        // Then
        assertFalse(result);
        verify(userRepository, times(1)).findByEmail(any(Email.class));
    }

    @Test
    void should_CreateUser_When_ValidData() {
        // Given
        String username = "newuser";
        String password = "password123";
        String nickname = "New User";
        String email = "new@example.com";
        String phone = "13800138000";
        
        // Mock repository behavior
        when(userRepository.findByUsername(any(Username.class))).thenReturn(null);
        when(userRepository.findByEmail(any(Email.class))).thenReturn(null);
        
        User savedUser = new User.Builder()
                .id(1L)
                .username(username)
                .password(password)
                .nickname(nickname)
                .email(email)
                .phone(phone)
                .status(1)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        
        // When
        User createdUser = userDomainService.createUser(username, password, nickname, email, phone);
        
        // Then
        assertNotNull(createdUser);
        assertEquals(savedUser, createdUser);
        assertEquals(1L, createdUser.getId().getValue());
        
        // 验证调用次数
        verify(userRepository, times(1)).findByUsername(any(Username.class));
        verify(userRepository, times(1)).findByEmail(any(Email.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void should_ThrowException_When_UsernameAlreadyExists() {
        // Given
        String username = "existinguser";
        String password = "password123";
        String nickname = "New User";
        String email = "new@example.com";
        String phone = "13800138000";
        
        // Mock repository behavior - username already exists
        when(userRepository.findByUsername(any(Username.class))).thenReturn(new User.Builder().build());
        
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userDomainService.createUser(username, password, nickname, email, phone);
        });
        assertEquals("Username already exists", exception.getMessage());
        
        // 验证只调用了一次findByUsername，没有调用其他方法
        verify(userRepository, times(1)).findByUsername(any(Username.class));
        verify(userRepository, never()).findByEmail(any(Email.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void should_ThrowException_When_EmailAlreadyExists() {
        // Given
        String username = "newuser";
        String password = "password123";
        String nickname = "New User";
        String email = "existing@example.com";
        String phone = "13800138000";
        
        // Mock repository behavior - email already exists
        when(userRepository.findByUsername(any(Username.class))).thenReturn(null);
        when(userRepository.findByEmail(any(Email.class))).thenReturn(new User.Builder().build());
        
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userDomainService.createUser(username, password, nickname, email, phone);
        });
        assertEquals("Email already exists", exception.getMessage());
        
        // 验证调用次数
        verify(userRepository, times(1)).findByUsername(any(Username.class));
        verify(userRepository, times(1)).findByEmail(any(Email.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void should_UpdateUser_When_ValidData() {
        // Given
        User user = new User.Builder()
                .id(1L)
                .username("testuser")
                .password("password123")
                .nickname("Test User")
                .email("test@example.com")
                .phone("13800138000")
                .status(1)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        
        // Mock repository behavior
        User updatedUser = new User.Builder()
                .id(1L)
                .username("testuser")
                .password("password123")
                .nickname("Updated User")
                .email("test@example.com")
                .phone("13800138000")
                .status(0)
                .createdAt(user.getCreatedAt())
                .updatedAt(new Date())
                .build();
        when(userRepository.update(any(User.class))).thenReturn(updatedUser);
        
        // When
        User result = userDomainService.updateUser(user);
        
        // Then
        assertNotNull(result);
        assertEquals(updatedUser, result);
        assertEquals(0, result.getStatus());
        
        // 验证调用次数
        verify(userRepository, times(1)).update(any(User.class));
    }
}
