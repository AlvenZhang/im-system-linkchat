package com.linkchat.application.service;

import com.linkchat.application.command.RegisterUserCommand;
import com.linkchat.application.command.UpdateUserCommand;
import com.linkchat.application.dto.UserDTO;
import com.linkchat.application.query.UserQuery;
import com.linkchat.domain.user.entity.User;
import com.linkchat.domain.user.repository.UserRepository;
import com.linkchat.domain.user.service.UserDomainService;
import com.linkchat.domain.user.value.UserId;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

public class UserApplicationService {
    private final UserRepository userRepository;
    private final UserDomainService userDomainService;
    private final PasswordEncoder passwordEncoder;

    public UserApplicationService(UserRepository userRepository, UserDomainService userDomainService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userDomainService = userDomainService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 用户注册
     * @param command 注册命令
     * @return 注册成功的用户DTO
     */
    public UserDTO registerUser(RegisterUserCommand command) {
        // 加密密码
        String encryptedPassword = passwordEncoder.encode(command.getPassword());

        // 调用领域服务创建用户
        User user = userDomainService.createUser(
                command.getUsername(),
                encryptedPassword,
                command.getNickname(),
                command.getEmail(),
                command.getPhone()
        );

        // 转换为DTO返回
        return convertToDTO(user);
    }

    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户DTO
     */
    public UserDTO getUserById(Long id) {
        UserId userId = UserId.of(id);
        User user = userRepository.findById(userId);
        return user != null ? convertToDTO(user) : null;
    }

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户DTO
     */
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(new com.linkchat.domain.user.value.Username(username));
        return user != null ? convertToDTO(user) : null;
    }

    /**
     * 更新用户信息
     * @param command 更新命令
     * @return 更新后的用户DTO
     */
    public UserDTO updateUser(UpdateUserCommand command) {
        // 查询用户
        UserId userId = UserId.of(command.getId());
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // 更新用户信息
        if (command.getNickname() != null) {
            user.updateNickname(command.getNickname());
        }
        if (command.getAvatar() != null) {
            user.updateAvatar(command.getAvatar());
        }
        if (command.getPhone() != null) {
            user.updatePhone(command.getPhone());
        }
        if (command.getStatus() != null) {
            user.updateStatus(command.getStatus());
        }

        // 保存更新
        User updatedUser = userRepository.update(user);
        return convertToDTO(updatedUser);
    }

    /**
     * 将User实体转换为UserDTO
     * @param user User实体
     * @return UserDTO
     */
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId().getValue());
        dto.setUsername(user.getUsername().getValue());
        dto.setNickname(user.getNickname());
        dto.setAvatar(user.getAvatar());
        dto.setEmail(user.getEmail().getValue());
        dto.setPhone(user.getPhone());
        dto.setStatus(user.getStatus());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
}