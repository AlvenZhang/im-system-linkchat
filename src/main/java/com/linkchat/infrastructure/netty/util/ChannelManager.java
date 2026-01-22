package com.linkchat.infrastructure.netty.util;

import com.linkchat.infrastructure.netty.model.WebSocketMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocket连接管理器
 * 管理在线用户的WebSocket连接
 */
public class ChannelManager {
    
    /**
     * Channel属性键：用户ID
     */
    public static final AttributeKey<String> USER_ID = AttributeKey.newInstance("userId");
    
    /**
     * 用户ID与Channel集合的映射，支持多设备登录
     */
    private static final Map<String, Set<Channel>> USER_CHANNEL_MAP = new ConcurrentHashMap<>();
    
    /**
     * ChannelId与用户ID的映射
     */
    private static final Map<ChannelId, String> CHANNEL_USER_MAP = new ConcurrentHashMap<>();
    
    /**
     * 群组ID与用户ID集合的映射，缓存群成员信息
     */
    private static final Map<String, Set<String>> GROUP_MEMBERS_MAP = new ConcurrentHashMap<>();
    
    /**
     * 添加Channel到管理器
     * @param userId 用户ID
     * @param channel Channel
     */
    public static void addChannel(String userId, Channel channel) {
        // 将用户ID设置到Channel属性中
        channel.attr(USER_ID).set(userId);
        // 添加映射关系
        USER_CHANNEL_MAP.computeIfAbsent(userId, k -> new CopyOnWriteArraySet<>()).add(channel);
        CHANNEL_USER_MAP.put(channel.id(), userId);
    }
    
    /**
     * 从管理器中移除Channel
     * @param channel Channel
     */
    public static void removeChannel(Channel channel) {
        String userId = channel.attr(USER_ID).get();
        if (userId != null) {
            Set<Channel> channels = USER_CHANNEL_MAP.get(userId);
            if (channels != null) {
                channels.remove(channel);
                // 如果该用户没有其他连接，移除该用户的映射
                if (channels.isEmpty()) {
                    USER_CHANNEL_MAP.remove(userId);
                }
            }
        }
        CHANNEL_USER_MAP.remove(channel.id());
    }
    
    /**
     * 根据用户ID获取所有Channel
     * @param userId 用户ID
     * @return Channel集合，如果用户不在线则返回空集合
     */
    public static Set<Channel> getChannels(String userId) {
        return USER_CHANNEL_MAP.getOrDefault(userId, Collections.emptySet());
    }
    
    /**
     * 根据用户ID获取任意一个Channel
     * @param userId 用户ID
     * @return Channel，如果用户不在线则返回null
     */
    public static Channel getChannel(String userId) {
        Set<Channel> channels = USER_CHANNEL_MAP.get(userId);
        if (channels != null && !channels.isEmpty()) {
            return channels.iterator().next();
        }
        return null;
    }
    
    /**
     * 根据Channel获取用户ID
     * @param channel Channel
     * @return 用户ID，如果Channel未关联用户则返回null
     */
    public static String getUserId(Channel channel) {
        return channel.attr(USER_ID).get();
    }
    
    /**
     * 检查用户是否在线
     * @param userId 用户ID
     * @return true表示在线，false表示离线
     */
    public static boolean isOnline(String userId) {
        return USER_CHANNEL_MAP.containsKey(userId);
    }
    
    /**
     * 获取在线用户数量
     * @return 在线用户数量
     */
    public static int getOnlineUserCount() {
        return USER_CHANNEL_MAP.size();
    }
    
    /**
     * 向指定用户发送消息（所有设备）
     * @param userId 用户ID
     * @param message 消息
     * @return true表示至少有一个设备在线，false表示用户不在线
     */
    public static boolean sendMessage(String userId, WebSocketMessage message) {
        Set<Channel> channels = USER_CHANNEL_MAP.get(userId);
        if (channels == null || channels.isEmpty()) {
            return false;
        }
        
        boolean sent = false;
        for (Channel channel : channels) {
            if (channel.isActive()) {
                channel.writeAndFlush(message);
                sent = true;
            }
        }
        return sent;
    }
    
    /**
     * 向指定Channel发送消息
     * @param channel Channel
     * @param message 消息
     */
    public static void sendMessage(Channel channel, WebSocketMessage message) {
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(message);
        }
    }
    
    /**
     * 向所有在线用户广播消息
     * @param message 消息
     */
    public static void broadcastMessage(WebSocketMessage message) {
        for (Set<Channel> channels : USER_CHANNEL_MAP.values()) {
            for (Channel channel : channels) {
                if (channel.isActive()) {
                    channel.writeAndFlush(message);
                }
            }
        }
    }
    
    /**
     * 向除了指定用户之外的所有在线用户广播消息
     * @param exceptUserId 排除的用户ID
     * @param message 消息
     */
    public static void broadcastMessageExcept(String exceptUserId, WebSocketMessage message) {
        for (Map.Entry<String, Set<Channel>> entry : USER_CHANNEL_MAP.entrySet()) {
            String userId = entry.getKey();
            if (!userId.equals(exceptUserId)) {
                Set<Channel> channels = entry.getValue();
                for (Channel channel : channels) {
                    if (channel.isActive()) {
                        channel.writeAndFlush(message);
                    }
                }
            }
        }
    }
    
    /**
     * 向群成员发送消息
     * @param groupId 群组ID
     * @param message 消息
     * @param exceptUserId 排除的用户ID（发送者）
     */
    public static void sendGroupMessage(String groupId, WebSocketMessage message, String exceptUserId) {
        Set<String> memberIds = GROUP_MEMBERS_MAP.get(groupId);
        if (memberIds != null && !memberIds.isEmpty()) {
            for (String memberId : memberIds) {
                if (!memberId.equals(exceptUserId)) {
                    sendMessage(memberId, message);
                }
            }
        }
    }
    
    /**
     * 更新群组缓存
     * @param groupId 群组ID
     * @param memberIds 群成员ID集合
     */
    public static void updateGroupMembers(String groupId, Set<String> memberIds) {
        if (memberIds != null && !memberIds.isEmpty()) {
            GROUP_MEMBERS_MAP.put(groupId, new CopyOnWriteArraySet<>(memberIds));
        } else {
            GROUP_MEMBERS_MAP.remove(groupId);
        }
    }
    
    /**
     * 从群组缓存中移除群成员
     * @param groupId 群组ID
     * @param memberId 成员ID
     */
    public static void removeGroupMember(String groupId, String memberId) {
        Set<String> memberIds = GROUP_MEMBERS_MAP.get(groupId);
        if (memberIds != null) {
            memberIds.remove(memberId);
            if (memberIds.isEmpty()) {
                GROUP_MEMBERS_MAP.remove(groupId);
            }
        }
    }
    
    /**
     * 向群组缓存中添加群成员
     * @param groupId 群组ID
     * @param memberId 成员ID
     */
    public static void addGroupMember(String groupId, String memberId) {
        GROUP_MEMBERS_MAP.computeIfAbsent(groupId, k -> new CopyOnWriteArraySet<>()).add(memberId);
    }
    
    /**
     * 获取群成员列表
     * @param groupId 群组ID
     * @return 群成员ID集合
     */
    public static Set<String> getGroupMembers(String groupId) {
        return GROUP_MEMBERS_MAP.getOrDefault(groupId, Collections.emptySet());
    }
    
    /**
     * 清理群组缓存
     * @param groupId 群组ID
     */
    public static void clearGroupCache(String groupId) {
        GROUP_MEMBERS_MAP.remove(groupId);
    }
}