package com.linkchat.infrastructure.persistence;

import com.linkchat.domain.user.entity.User;
import com.linkchat.domain.user.repository.UserRepository;
import com.linkchat.domain.user.value.Email;
import com.linkchat.domain.user.value.UserId;
import com.linkchat.domain.user.value.Username;
import com.linkchat.infrastructure.persistence.entity.UserEntity;
import com.linkchat.infrastructure.persistence.mapper.UserMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final UserMapper userMapper;

    public UserRepositoryImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User findById(UserId id) {
        UserEntity dbUser = userMapper.selectById(id.getValue());
        return dbUser != null ? convertToDomain(dbUser) : null;
    }

    @Override
    public User findByUsername(Username username) {
        UserEntity dbUser = userMapper.selectByUsername(username.getValue());
        return dbUser != null ? convertToDomain(dbUser) : null;
    }

    @Override
    public User findByEmail(Email email) {
        UserEntity dbUser = userMapper.selectByEmail(email.getValue());
        return dbUser != null ? convertToDomain(dbUser) : null;
    }

    @Override
    public User save(User user) {
        UserEntity dbUser = convertToDb(user);
        userMapper.insert(dbUser);
        // 设置生成的ID回领域实体
        return new User.Builder()
                .id(dbUser.getId())
                .username(user.getUsername().getValue())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .email(user.getEmail().getValue())
                .phone(user.getPhone())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Override
    public User update(User user) {
        UserEntity dbUser = convertToDb(user);
        userMapper.update(dbUser);
        return convertToDomain(dbUser);
    }

    @Override
    public boolean delete(UserId id) {
        return userMapper.delete(id.getValue()) > 0;
    }

    /**
     * 将数据库用户转换为领域用户
     * @param dbUser 数据库用户
     * @return 领域用户
     */
    private User convertToDomain(UserEntity dbUser) {
        return new User.Builder()
                .id(dbUser.getId())
                .username(dbUser.getUsername())
                .password(dbUser.getPassword())
                .nickname(dbUser.getNickname())
                .avatar(dbUser.getAvatar())
                .email(dbUser.getEmail())
                .phone(dbUser.getPhone())
                .status(dbUser.getStatus())
                .createdAt(dbUser.getCreatedAt())
                .updatedAt(dbUser.getUpdatedAt())
                .build();
    }

    /**
     * 将领域用户转换为数据库用户
     * @param user 领域用户
     * @return 数据库用户
     */
    private UserEntity convertToDb(User user) {
        UserEntity dbUser = new UserEntity();
        if (user.getId() != null) {
            dbUser.setId(user.getId().getValue());
        }
        if (user.getUsername() != null) {
            dbUser.setUsername(user.getUsername().getValue());
        }
        dbUser.setPassword(user.getPassword());
        dbUser.setNickname(user.getNickname());
        dbUser.setAvatar(user.getAvatar());
        if (user.getEmail() != null) {
            dbUser.setEmail(user.getEmail().getValue());
        }
        dbUser.setPhone(user.getPhone());
        dbUser.setStatus(user.getStatus());
        dbUser.setCreatedAt(user.getCreatedAt());
        dbUser.setUpdatedAt(user.getUpdatedAt());
        return dbUser;
    }
}