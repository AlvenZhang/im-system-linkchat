package com.linkchat.domain.message.entity;

import lombok.Getter;

import java.util.Date;

@Getter
public class Message {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private Integer type;
    private String content;
    private Integer status;
    private Boolean isGroup;
    private Date createdAt;

    private Message() {
    }

    public static class Builder {
        private Message message = new Message();

        public Builder id(Long id) {
            message.id = id;
            return this;
        }

        public Builder senderId(Long senderId) {
            message.senderId = senderId;
            return this;
        }

        public Builder receiverId(Long receiverId) {
            message.receiverId = receiverId;
            return this;
        }

        public Builder type(Integer type) {
            message.type = type;
            return this;
        }

        public Builder content(String content) {
            message.content = content;
            return this;
        }

        public Builder status(Integer status) {
            message.status = status;
            return this;
        }

        public Builder isGroup(Boolean isGroup) {
            message.isGroup = isGroup;
            return this;
        }

        public Builder createdAt(Date createdAt) {
            message.createdAt = createdAt;
            return this;
        }

        public Message build() {
            return message;
        }
    }

    /**
     * 标记消息为已送达
     */
    public void markAsDelivered() {
        this.status = 1;
    }

    /**
     * 标记消息为已读
     */
    public void markAsRead() {
        this.status = 2;
    }

    /**
     * 检查消息是否已读
     * @return 消息是否已读
     */
    public boolean isRead() {
        return this.status == 2;
    }

    /**
     * 检查是否是群消息
     * @return 是否是群消息
     */
    public boolean isGroupMessage() {
        return this.isGroup != null && this.isGroup;
    }

    /**
     * 检查是否是私聊消息
     * @return 是否是私聊消息
     */
    public boolean isPrivateMessage() {
        return this.isGroup == null || !this.isGroup;
    }
}