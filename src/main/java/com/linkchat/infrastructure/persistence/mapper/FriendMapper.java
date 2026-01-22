package com.linkchat.infrastructure.persistence.mapper;

import com.linkchat.infrastructure.persistence.entity.FriendEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FriendMapper {
    
    /**
     * 根据ID查询好友关系
     * @param id 好友关系ID
     * @return 好友关系信息
     */
    FriendEntity selectById(@Param("id") Long id);
    
    /**
     * 查询用户的好友关系
     * @param userId 用户ID
     * @param friendId 好友ID
     * @return 好友关系信息
     */
    FriendEntity selectByUserIdAndFriendId(@Param("userId") Long userId, @Param("friendId") Long friendId);
    
    /**
     * 获取用户的好友列表
     * @param userId 用户ID
     * @param status 好友状态，null表示所有状态
     * @return 好友关系列表
     */
    List<FriendEntity> selectByUserId(@Param("userId") Long userId, @Param("status") Integer status);
    
    /**
     * 获取用户的好友请求列表
     * @param friendId 好友ID
     * @return 好友请求列表
     */
    List<FriendEntity> selectFriendRequests(@Param("friendId") Long friendId);
    
    /**
     * 插入好友关系
     * @param friend 好友关系信息
     * @return 影响行数
     */
    int insert(FriendEntity friend);
    
    /**
     * 更新好友关系状态
     * @param id 好友关系ID
     * @param status 新状态
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    

    
    /**
     * 删除好友关系
     * @param id 好友关系ID
     * @return 影响行数
     */
    int delete(@Param("id") Long id);
    
    /**
     * 删除好友关系（根据用户ID和好友ID）
     * @param userId 用户ID
     * @param friendId 好友ID
     * @return 影响行数
     */
    int deleteByUserIdAndFriendId(@Param("userId") Long userId, @Param("friendId") Long friendId);
}