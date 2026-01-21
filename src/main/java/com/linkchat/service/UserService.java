package com.linkchat.service;

import com.linkchat.entity.User;
import com.linkchat.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户信息
     */
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    public User getUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    /**
     * 根据邮箱查询用户
     * @param email 邮箱
     * @return 用户信息
     */
    public User getUserByEmail(String email) {
        return userMapper.selectByEmail(email);
    }

    /**
     * 注册用户
     * @param user 用户信息
     * @return 注册结果
     */
    public boolean registerUser(User user) {
        // 检查用户名是否已存在
        if (getUserByUsername(user.getUsername()) != null) {
            return false;
        }

        // 检查邮箱是否已存在
        if (getUserByEmail(user.getEmail()) != null) {
            return false;
        }

        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        user.setStatus(1); // 默认离线状态

        // 插入用户
        return userMapper.insert(user) > 0;
    }

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 更新结果
     */
    public boolean updateUser(User user) {
        user.setUpdatedAt(new Date());
        return userMapper.update(user) > 0;
    }

    /**
     * 删除用户
     * @param id 用户ID
     * @return 删除结果
     */
    public boolean deleteUser(Long id) {
        return userMapper.delete(id) > 0;
    }
}