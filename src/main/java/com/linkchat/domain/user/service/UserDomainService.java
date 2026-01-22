package com.linkchat.domain.user.service;

import com.linkchat.domain.user.entity.User;
import com.linkchat.domain.user.repository.UserRepository;
import com.linkchat.domain.user.value.Email;
import com.linkchat.domain.user.value.Username;

import java.util.Date;

public class UserDomainService {
    private final UserRepository userRepository;

    public UserDomainService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 验证用户名是否可用
     * @param username 用户名
     * @return 用户名是否可用
     */
    public boolean isUsernameAvailable(String username) {
        Username usernameValue = Username.of(username);
        return userRepository.findByUsername(usernameValue) == null;
    }

    /**
     * 验证邮箱是否可用
     * @param email 邮箱
     * @return 邮箱是否可用
     */
    public boolean isEmailAvailable(String email) {
        Email emailValue = Email.of(email);
        return userRepository.findByEmail(emailValue) == null;
    }

    /**
     * 创建新用户
     * @param username 用户名
     * @param password 密码
     * @param nickname 昵称
     * @param email 邮箱
     * @param phone 电话
     * @return 创建的用户
     */
    public User createUser(String username, String password, String nickname, String email, String phone) {
        // 验证用户名和邮箱是否可用
        if (!isUsernameAvailable(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (!isEmailAvailable(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        // 构建用户对象
        User user = new User.Builder()
                .username(username)
                .password(password) // 密码应该在应用层进行加密
                .nickname(nickname)
                .email(email)
                .phone(phone)
                .status(1) // 默认在线状态
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        // 保存用户
        return userRepository.save(user);
    }

    /**
     * 更新用户信息
     * @param user 用户对象
     * @return 更新后的用户
     */
    public User updateUser(User user) {
        user.updateStatus(user.getStatus()); // 触发更新时间
        return userRepository.update(user);
    }
}