package com.linkchat.infrastructure.persistence.mapper;

import com.linkchat.infrastructure.persistence.entity.GroupMemberEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GroupMemberMapper {
    
    /**
     * 根据ID查询群成员
     * @param id 群成员ID
     * @return 群成员信息
     */
    GroupMemberEntity selectById(@Param("id") Long id);
    
    /**
     * 根据群组ID和用户ID查询群成员
     * @param groupId 群组ID
     * @param userId 用户ID
     * @return 群成员信息
     */
    GroupMemberEntity selectByGroupIdAndUserId(@Param("groupId") Long groupId, @Param("userId") Long userId);
    
    /**
     * 根据群组ID查询所有群成员
     * @param groupId 群组ID
     * @return 群成员列表
     */
    List<GroupMemberEntity> selectByGroupId(@Param("groupId") Long groupId);
    
    /**
     * 根据用户ID查询所有群成员关系
     * @param userId 用户ID
     * @return 群成员列表
     */
    List<GroupMemberEntity> selectByUserId(@Param("userId") Long userId);
    
    /**
     * 添加群成员
     * @param groupMember 群成员信息
     * @return 影响行数
     */
    int insert(GroupMemberEntity groupMember);
    
    /**
     * 更新群成员角色
     * @param id 群成员ID
     * @param role 新角色
     * @return 影响行数
     */
    int updateRole(@Param("id") Long id, @Param("role") Integer role);
    
    /**
     * 批量添加群成员
     * @param groupMembers 群成员列表
     * @return 影响行数
     */
    int batchInsert(@Param("groupMembers") List<GroupMemberEntity> groupMembers);
    
    /**
     * 删除群成员
     * @param id 群成员ID
     * @return 影响行数
     */
    int delete(@Param("id") Long id);
    
    /**
     * 根据群组ID和用户ID删除群成员
     * @param groupId 群组ID
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteByGroupIdAndUserId(@Param("groupId") Long groupId, @Param("userId") Long userId);
    
    /**
     * 根据群组ID删除所有群成员
     * @param groupId 群组ID
     * @return 影响行数
     */
    int deleteByGroupId(@Param("groupId") Long groupId);
}