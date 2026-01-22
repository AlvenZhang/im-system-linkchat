package com.linkchat.infrastructure.netty.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * WebSocket消息模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketMessage implements Serializable {
    
    /**
     * 消息类型常量
     */
    public static final int TEXT = 0;             // 文本消息
    public static final int IMAGE = 1;            // 图片消息
    public static final int VOICE = 2;            // 语音消息
    public static final int VIDEO = 3;            // 视频消息
    public static final int FILE = 4;             // 文件消息
    public static final int HEARTBEAT = 5;        // 心跳消息
    public static final int ONLINE_STATUS = 6;    // 在线状态消息
    public static final int JOIN_GROUP = 7;       // 加入群组消息
    public static final int LEAVE_GROUP = 8;      // 离开群组消息
    public static final int CREATE_GROUP = 9;     // 创建群组消息
    
    /**
     * 消息类型
     */
    private int type;
    
    /**
     * 发送者ID
     */
    private String from;
    
    /**
     * 接收者ID（用户ID或群组ID）
     */
    private String to;
    
    /**
     * 消息内容
     */
    private String content;
    
    /**
     * 时间戳
     */
    private long timestamp;
    
    /**
     * 是否群消息
     */
    private boolean isGroup;
    
    /**
     * 额外信息
     */
    private Map<String, Object> extra;
    
    /**
     * 构建心跳消息
     * @return 心跳消息
     */
    public static WebSocketMessage heartbeat() {
        WebSocketMessage message = new WebSocketMessage();
        message.setType(HEARTBEAT);
        message.setTimestamp(System.currentTimeMillis());
        return message;
    }
    
    /**
     * 构建文本消息
     * @param from 发送者ID
     * @param to 接收者ID
     * @param content 消息内容
     * @param isGroup 是否群消息
     * @return 文本消息
     */
    public static WebSocketMessage textMessage(String from, String to, String content, boolean isGroup) {
        WebSocketMessage message = new WebSocketMessage();
        message.setType(TEXT);
        message.setFrom(from);
        message.setTo(to);
        message.setContent(content);
        message.setTimestamp(System.currentTimeMillis());
        message.setGroup(isGroup);
        return message;
    }
}