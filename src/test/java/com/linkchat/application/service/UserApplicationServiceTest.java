package com.linkchat.application.service;

import com.linkchat.application.command.RegisterUserCommand;
import com.linkchat.application.command.UpdateUserCommand;
import com.linkchat.application.dto.UserDTO;
import com.linkchat.domain.user.entity.User;
import com.linkchat.domain.user.repository.UserRepository;
import com.linkchat.domain.user.service.UserDomainService;
import com.linkchat.domain.user.value.UserId;
import com.linkchat.domain.user.value.Username;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserApplicationServiceTest {

    private UserRepository userRepository;
    private UserDomainService userDomainService;
    private PasswordEncoder passwordEncoder;
    private UserApplicationService userApplicationService;

    @BeforeEach
    void setUp() {
        // 创建模拟对象
        userRepository = Mockito.mock(UserRepository.class);
        userDomainService = Mockito.mock(UserDomainService.class);
        passwordEncoder = new BCryptPasswordEncoder();
        
        // 初始化测试对象
        userApplicationService = new UserApplicationService(userRepository, userDomainService, passwordEncoder);
    }

    @Test
    void should_RegisterUserSuccessfully_When_ValidCommand() {
        // Given
        RegisterUserCommand command = new RegisterUserCommand();
        command.setUsername("testuser");
        command.setPassword("password123");
        command.setNickname("Test User");
        command.setEmail("test@example.com");
        command.setPhone("13800138000");
        
        // 创建预期的用户实体
        User expectedUser = new User.Builder()
                .id(1L)
                .username("testuser")
                .password(passwordEncoder.encode("password123"))
                .nickname("Test User")
                .email("test@example.com")
                .phone("13800138000")
                .status(1)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        
        // Mock领域服务行为
        when(userDomainService.createUser(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(expectedUser);
        
        // When
        UserDTO result = userApplicationService.registerUser(command);
        
        // Then
        assertNotNull(result);
        assertEquals(expectedUser.getId().getValue(), result.getId());
        assertEquals(expectedUser.getUsername().getValue(), result.getUsername());
        assertEquals(expectedUser.getNickname(), result.getNickname());
        assertEquals(expectedUser.getEmail().getValue(), result.getEmail());
        assertEquals(expectedUser.getPhone(), result.getPhone());
        assertEquals(expectedUser.getStatus(), result.getStatus());
        
        // 验证调用次数
        verify(userDomainService, times(1)).createUser(anyString(), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void should_ReturnUserDTO_When_GetUserById() {
        // Given
        Long userId = 1L;
        User user = new User.Builder()
                .id(userId)
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .nickname("Test User")
                .status(1)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        
        when(userRepository.findById(any(UserId.class))).thenReturn(user);
        
        // When
        UserDTO result = userApplicationService.getUserById(userId);
        
        // Then
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(user.getUsername().getValue(), result.getUsername());
        
        // 验证调用次数
        verify(userRepository, times(1)).findById(any(UserId.class));
    }

    @Test
    void should_ReturnNull_When_GetUserByIdNotFound() {
        // Given
        Long nonExistentId = 999L;
        when(userRepository.findById(any(UserId.class))).thenReturn(null);
        
        // When
        UserDTO result = userApplicationService.getUserById(nonExistentId);
        
        // Then
        assertNull(result);
        
        // 验证调用次数
        verify(userRepository, times(1)).findById(any(UserId.class));
    }

    @Test
    void should_ReturnUserDTO_When_GetUserByUsername() {
        // Given
        String username = "testuser";
        User user = new User.Builder()
                .id(1L)
                .username(username)
                .email("test@example.com")
                .password("password")
                .nickname("Test User")
                .status(1)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        
        when(userRepository.findByUsername(any(Username.class))).thenReturn(user);
        
        // When
        UserDTO result = userApplicationService.getUserByUsername(username);
        
        // Then
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals(user.getId().getValue(), result.getId());
        
        // 验证调用次数
        verify(userRepository, times(1)).findByUsername(any(Username.class));
    }

    @Test
    void should_ReturnNull_When_GetUserByUsernameNotFound() {
        // Given
        String nonExistentUsername = "nonexistentuser";
        when(userRepository.findByUsername(any(Username.class))).thenReturn(null);
        
        // When
        UserDTO result = userApplicationService.getUserByUsername(nonExistentUsername);
        
        // Then
        assertNull(result);
        
        // 验证调用次数
        verify(userRepository, times(1)).findByUsername(any(Username.class));
    }

    @Test
    void should_UpdateUserSuccessfully_When_ValidCommand() {
        // Given
        Long userId = 1L;
        UpdateUserCommand command = new UpdateUserCommand();
        command.setId(userId);
        command.setNickname("Updated Nickname");
        command.setAvatar("new_avatar.jpg");
        command.setStatus(0);
        
        // 创建原始用户
        User originalUser = new User.Builder()
                .id(userId)
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .nickname("Original Nickname")
                .avatar("old_avatar.jpg")
                .status(1)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        
        // 创建更新后的用户
        User updatedUser = new User.Builder()
                .id(userId)
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .nickname("Updated Nickname")
                .avatar("new_avatar.jpg")
                .status(0)
                .createdAt(originalUser.getCreatedAt())
                .updatedAt(new Date())
                .build();
        
        // Mock repository行为
        when(userRepository.findById(any(UserId.class))).thenReturn(originalUser);
        when(userRepository.update(any(User.class))).thenReturn(updatedUser);
        
        // When
        UserDTO result = userApplicationService.updateUser(command);
        
        // Then
        assertNotNull(result);
        assertEquals(updatedUser.getNickname(), result.getNickname());
        assertEquals(updatedUser.getAvatar(), result.getAvatar());
        assertEquals(updatedUser.getStatus(), result.getStatus());
        
        // 验证调用次数
        verify(userRepository, times(1)).findById(any(UserId.class));
        verify(userRepository, times(1)).update(any(User.class));
    }

    @Test
    void should_ThrowException_When_UpdateNonExistentUser() {
        // Given
        Long nonExistentId = 999L;
        UpdateUserCommand command = new UpdateUserCommand();
        command.setId(nonExistentId);
        command.setNickname("Updated Nickname");
        
        // Mock repository行为 - 用户不存在
        when(userRepository.findById(any(UserId.class))).thenReturn(null);
        
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userApplicationService.updateUser(command);
        });
        assertEquals("User not found", exception.getMessage());
        
        // 验证调用次数
        verify(userRepository, times(1)).findById(any(UserId.class));
        verify(userRepository, never()).update(any(User.class));
    }

    @Test
    void should_UpdatePartialFields_When_UpdateUserWithPartialData() {
        // Given
        Long userId = 1L;
        UpdateUserCommand command = new UpdateUserCommand();
        command.setId(userId);
        command.setNickname("Updated Nickname");
        // 只更新昵称，其他字段为null
        
        // 创建原始用户
        User originalUser = new User.Builder()
                .id(userId)
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .nickname("Original Nickname")
                .avatar("old_avatar.jpg")
                .status(1)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        
        // 创建更新后的用户（只更新昵称）
        User updatedUser = new User.Builder()
                .id(userId)
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .nickname("Updated Nickname")
                .avatar("old_avatar.jpg") // 保持不变
                .status(1) // 保持不变
                .createdAt(originalUser.getCreatedAt())
                .updatedAt(new Date())
                .build();
        
        // Mock repository行为
        when(userRepository.findById(any(UserId.class))).thenReturn(originalUser);
        when(userRepository.update(any(User.class))).thenReturn(updatedUser);
        
        // When
        UserDTO result = userApplicationService.updateUser(command);
        
        // Then
        assertNotNull(result);
        assertEquals("Updated Nickname", result.getNickname());
        assertEquals("old_avatar.jpg", result.getAvatar()); // 验证未更新字段保持不变
        assertEquals(1, result.getStatus()); // 验证未更新字段保持不变
        
        // 验证调用次数
        verify(userRepository, times(1)).findById(any(UserId.class));
        verify(userRepository, times(1)).update(any(User.class));
    }
}
