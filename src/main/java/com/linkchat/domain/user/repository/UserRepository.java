package com.linkchat.domain.user.repository;

import com.linkchat.domain.user.entity.User;
import com.linkchat.domain.user.value.Email;
import com.linkchat.domain.user.value.UserId;
import com.linkchat.domain.user.value.Username;

public interface UserRepository {
    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户信息
     */
    User findById(UserId id);

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    User findByUsername(Username username);

    /**
     * 根据邮箱查询用户
     * @param email 邮箱
     * @return 用户信息
     */
    User findByEmail(Email email);

    /**
     * 保存用户
     * @param user 用户信息
     * @return 保存后的用户
     */
    User save(User user);

    /**
     * 更新用户
     * @param user 用户信息
     * @return 更新后的用户
     */
    User update(User user);

    /**
     * 删除用户
     * @param id 用户ID
     * @return 删除结果
     */
    boolean delete(UserId id);
}