package com.linkchat.infrastructure.persistence.mapper;

import com.linkchat.infrastructure.persistence.entity.MessageEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper {
    
    /**
     * 根据ID查询消息
     * @param id 消息ID
     * @return 消息信息
     */
    MessageEntity selectById(@Param("id") Long id);
    
    /**
     * 获取单聊消息记录
     * @param senderId 发送者ID
     * @param receiverId 接收者ID
     * @param limit 限制数量
     * @param offset 偏移量
     * @return 消息列表
     */
    List<MessageEntity> selectChatHistory(@Param("senderId") Long senderId, 
                                   @Param("receiverId") Long receiverId, 
                                   @Param("limit") Integer limit, 
                                   @Param("offset") Integer offset);
    
    /**
     * 获取群聊消息记录
     * @param groupId 群组ID
     * @param limit 限制数量
     * @param offset 偏移量
     * @return 消息列表
     */
    List<MessageEntity> selectGroupChatHistory(@Param("groupId") Long groupId, 
                                        @Param("limit") Integer limit, 
                                        @Param("offset") Integer offset);
    
    /**
     * 获取用户未读消息数量
     * @param receiverId 接收者ID
     * @param isGroup 是否群消息
     * @return 未读消息数量
     */
    Integer countUnreadMessages(@Param("receiverId") Long receiverId, @Param("isGroup") Integer isGroup);
    
    /**
     * 发送消息
     * @param message 消息信息
     * @return 影响行数
     */
    int insert(MessageEntity message);
    
    /**
     * 更新消息状态
     * @param id 消息ID
     * @param status 新状态
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    /**
     * 批量更新消息状态
     * @param ids 消息ID列表
     * @param status 新状态
     * @return 影响行数
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") Integer status);
    
    /**
     * 更新用户与特定聊天对象的所有消息状态
     * @param userId 用户ID
     * @param chatId 聊天对象ID（用户ID或群组ID）
     * @param isGroup 是否群消息
     * @param status 新状态
     * @return 影响行数
     */
    int updateAllStatusByChatId(@Param("userId") Long userId, 
                               @Param("chatId") Long chatId, 
                               @Param("isGroup") Integer isGroup, 
                               @Param("status") Integer status);
    
    /**
     * 删除消息
     * @param id 消息ID
     * @return 影响行数
     */
    int delete(@Param("id") Long id);
}