package com.linkchat.infrastructure.netty;

import com.linkchat.infrastructure.netty.model.WebSocketMessage;
import com.linkchat.infrastructure.netty.util.ChannelManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * Netty服务器消息处理器
 * 处理WebSocket连接、消息和心跳检测
 */
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    
    /**
     * 当客户端连接建立时调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端连接建立：{}", ctx.channel().remoteAddress());
    }
    
    /**
     * 当客户端连接关闭时调用
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 从连接管理器中移除Channel
        ChannelManager.removeChannel(ctx.channel());
        String userId = ChannelManager.getUserId(ctx.channel());
        if (userId != null) {
            log.info("用户离线：{}", userId);
            // 广播用户离线状态
            broadcastOnlineStatus(userId, false);
        }
        log.info("客户端连接关闭：{}", ctx.channel().remoteAddress());
    }
    
    /**
     * 当收到消息时调用
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof WebSocketMessage) {
            WebSocketMessage message = (WebSocketMessage) msg;
            handleMessage(ctx, message);
        } else if (msg instanceof TextWebSocketFrame) {
            // 处理原始的TextWebSocketFrame
            TextWebSocketFrame frame = (TextWebSocketFrame) msg;
            log.info("收到原始消息：{}", frame.text());
        }
    }
    
    /**
     * 处理WebSocket消息
     * @param ctx ChannelHandlerContext
     * @param message WebSocketMessage
     */
    private void handleMessage(ChannelHandlerContext ctx, WebSocketMessage message) {
        switch (message.getType()) {
            case WebSocketMessage.HEARTBEAT:
                handleHeartbeat(ctx, message);
                break;
            case WebSocketMessage.TEXT:
            case WebSocketMessage.IMAGE:
            case WebSocketMessage.VOICE:
            case WebSocketMessage.VIDEO:
            case WebSocketMessage.FILE:
                handleChatMessage(ctx, message);
                break;
            case WebSocketMessage.ONLINE_STATUS:
                handleOnlineStatus(ctx, message);
                break;
            case WebSocketMessage.JOIN_GROUP:
                handleJoinGroup(ctx, message);
                break;
            case WebSocketMessage.LEAVE_GROUP:
                handleLeaveGroup(ctx, message);
                break;
            case WebSocketMessage.CREATE_GROUP:
                handleCreateGroup(ctx, message);
                break;
            default:
                log.info("收到未处理的消息类型：{}", message.getType());
                break;
        }
    }
    
    /**
     * 处理心跳消息
     * @param ctx ChannelHandlerContext
     * @param message 心跳消息
     */
    private void handleHeartbeat(ChannelHandlerContext ctx, WebSocketMessage message) {
        // 回复心跳消息
        WebSocketMessage heartbeat = WebSocketMessage.heartbeat();
        ChannelManager.sendMessage(ctx.channel(), heartbeat);
    }
    
    /**
     * 处理聊天消息（文本、图片、语音、视频、文件）
     * @param ctx ChannelHandlerContext
     * @param message 聊天消息
     */
    private void handleChatMessage(ChannelHandlerContext ctx, WebSocketMessage message) {
        log.info("收到聊天消息：type={}, from={}, to={}, content={}", 
                message.getType(), message.getFrom(), message.getTo(), message.getContent());
        
        // 保存用户ID和Channel的映射
        if (message.getFrom() != null) {
            ChannelManager.addChannel(message.getFrom(), ctx.channel());
        }
        
        if (message.isGroup()) {
            // 群消息
            ChannelManager.sendGroupMessage(message.getTo(), message, message.getFrom());
        } else {
            // 单聊消息
            boolean sent = ChannelManager.sendMessage(message.getTo(), message);
            if (!sent) {
                log.warn("消息发送失败，接收者不在线：{}", message.getTo());
                // 可以返回消息发送失败的通知
            }
        }
    }
    
    /**
     * 处理加入群组消息
     * @param ctx ChannelHandlerContext
     * @param message 加入群组消息
     */
    private void handleJoinGroup(ChannelHandlerContext ctx, WebSocketMessage message) {
        log.info("用户加入群组：userId={}, groupId={}", message.getFrom(), message.getTo());
        
        // 保存用户ID和Channel的映射
        if (message.getFrom() != null) {
            ChannelManager.addChannel(message.getFrom(), ctx.channel());
        }
        
        // 更新群组缓存，添加新成员
        ChannelManager.addGroupMember(message.getTo(), message.getFrom());
        
        // 广播群成员加入通知
        WebSocketMessage joinMessage = new WebSocketMessage();
        joinMessage.setType(WebSocketMessage.JOIN_GROUP);
        joinMessage.setFrom(message.getFrom());
        joinMessage.setTo(message.getTo());
        joinMessage.setTimestamp(System.currentTimeMillis());
        ChannelManager.sendGroupMessage(message.getTo(), joinMessage, null);
    }
    
    /**
     * 处理离开群组消息
     * @param ctx ChannelHandlerContext
     * @param message 离开群组消息
     */
    private void handleLeaveGroup(ChannelHandlerContext ctx, WebSocketMessage message) {
        log.info("用户离开群组：userId={}, groupId={}", message.getFrom(), message.getTo());
        
        // 更新群组缓存，移除成员
        ChannelManager.removeGroupMember(message.getTo(), message.getFrom());
        
        // 广播群成员离开通知
        WebSocketMessage leaveMessage = new WebSocketMessage();
        leaveMessage.setType(WebSocketMessage.LEAVE_GROUP);
        leaveMessage.setFrom(message.getFrom());
        leaveMessage.setTo(message.getTo());
        leaveMessage.setTimestamp(System.currentTimeMillis());
        ChannelManager.sendGroupMessage(message.getTo(), leaveMessage, null);
    }
    
    /**
     * 处理创建群组消息
     * @param ctx ChannelHandlerContext
     * @param message 创建群组消息
     */
    private void handleCreateGroup(ChannelHandlerContext ctx, WebSocketMessage message) {
        log.info("用户创建群组：userId={}, groupId={}", message.getFrom(), message.getTo());
        
        // 保存用户ID和Channel的映射
        if (message.getFrom() != null) {
            ChannelManager.addChannel(message.getFrom(), ctx.channel());
        }
        
        // 更新群组缓存，添加创建者作为第一个成员
        ChannelManager.addGroupMember(message.getTo(), message.getFrom());
        
        // 广播群组创建通知
        WebSocketMessage createMessage = new WebSocketMessage();
        createMessage.setType(WebSocketMessage.CREATE_GROUP);
        createMessage.setFrom(message.getFrom());
        createMessage.setTo(message.getTo());
        createMessage.setTimestamp(System.currentTimeMillis());
        ChannelManager.sendGroupMessage(message.getTo(), createMessage, null);
    }
    
    /**
     * 处理在线状态消息
     * @param ctx ChannelHandlerContext
     * @param message 在线状态消息
     */
    private void handleOnlineStatus(ChannelHandlerContext ctx, WebSocketMessage message) {
        String userId = message.getFrom();
        if (userId != null) {
            // 保存用户ID和Channel的映射
            ChannelManager.addChannel(userId, ctx.channel());
            log.info("用户上线：{}", userId);
            // 广播用户上线状态
            broadcastOnlineStatus(userId, true);
        }
    }
    
    /**
     * 广播用户在线状态
     * @param userId 用户ID
     * @param online 是否在线
     */
    private void broadcastOnlineStatus(String userId, boolean online) {
        WebSocketMessage message = new WebSocketMessage();
        message.setType(WebSocketMessage.ONLINE_STATUS);
        message.setFrom(userId);
        message.setContent(online ? "online" : "offline");
        message.setTimestamp(System.currentTimeMillis());
        
        // 广播给所有在线用户
        ChannelManager.broadcastMessageExcept(userId, message);
    }
    
    /**
     * 当发生空闲事件时调用
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                // 读空闲，客户端超过指定时间没有发送消息
                log.info("客户端读空闲，关闭连接：{}", ctx.channel().remoteAddress());
                ctx.close();
            } else if (event.state() == IdleState.WRITER_IDLE) {
                // 写空闲，服务端超过指定时间没有发送消息
                // 发送心跳消息
                WebSocketMessage heartbeat = WebSocketMessage.heartbeat();
                ctx.writeAndFlush(heartbeat);
            }
        }
    }
    
    /**
     * 当发生异常时调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("处理消息时发生异常：{}", cause.getMessage(), cause);
        ctx.close();
    }
}