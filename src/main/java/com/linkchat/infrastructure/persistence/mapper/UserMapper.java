package com.linkchat.infrastructure.persistence.mapper;

import com.linkchat.infrastructure.persistence.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    
    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户信息
     */
    UserEntity selectById(@Param("id") Long id);
    
    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    UserEntity selectByUsername(@Param("username") String username);
    
    /**
     * 根据邮箱查询用户
     * @param email 邮箱
     * @return 用户信息
     */
    UserEntity selectByEmail(@Param("email") String email);
    
    /**
     * 插入用户
     * @param user 用户信息
     * @return 影响行数
     */
    int insert(UserEntity user);
    
    /**
     * 更新用户
     * @param user 用户信息
     * @return 影响行数
     */
    int update(UserEntity user);
    
    /**
     * 删除用户
     * @param id 用户ID
     * @return 影响行数
     */
    int delete(@Param("id") Long id);
    
    /**
     * 查询所有用户
     * @return 用户列表
     */
    List<UserEntity> selectAll();
}