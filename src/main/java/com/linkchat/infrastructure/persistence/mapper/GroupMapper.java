package com.linkchat.infrastructure.persistence.mapper;

import com.linkchat.infrastructure.persistence.entity.GroupEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GroupMapper {
    
    /**
     * 根据ID查询群组
     * @param id 群组ID
     * @return 群组信息
     */
    GroupEntity selectById(@Param("id") Long id);
    
    /**
     * 查询用户创建的群组
     * @param creatorId 创建者ID
     * @return 群组列表
     */
    List<GroupEntity> selectByCreatorId(@Param("creatorId") Long creatorId);
    
    /**
     * 查询所有群组
     * @return 群组列表
     */
    List<GroupEntity> selectAll();
    
    /**
     * 创建群组
     * @param group 群组信息
     * @return 影响行数
     */
    int insert(GroupEntity group);
    
    /**
     * 更新群组信息
     * @param group 群组信息
     * @return 影响行数
     */
    int update(GroupEntity group);
    
    /**
     * 更新群组成员数量
     * @param id 群组ID
     * @param count 增加或减少的数量
     * @return 影响行数
     */
    int updateMemberCount(@Param("id") Long id, @Param("count") Integer count);
    
    /**
     * 删除群组
     * @param id 群组ID
     * @return 影响行数
     */
    int delete(@Param("id") Long id);
}