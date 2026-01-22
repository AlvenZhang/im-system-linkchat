package com.linkchat.domain.friend.entity;

import lombok.Getter;

import java.util.Date;

@Getter
public class Friend {
    private Long id;
    private Long userId;
    private Long friendId;
    private Integer status;
    private Date createdAt;
    private Date updatedAt;

    private Friend() {
    }

    public static class Builder {
        private Friend friend = new Friend();

        public Builder id(Long id) {
            friend.id = id;
            return this;
        }

        public Builder userId(Long userId) {
            friend.userId = userId;
            return this;
        }

        public Builder friendId(Long friendId) {
            friend.friendId = friendId;
            return this;
        }

        public Builder status(Integer status) {
            friend.status = status;
            return this;
        }

        public Builder createdAt(Date createdAt) {
            friend.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(Date updatedAt) {
            friend.updatedAt = updatedAt;
            return this;
        }

        public Friend build() {
            return friend;
        }
    }

    /**
     * 同意好友请求
     */
    public void acceptFriendRequest() {
        this.status = 1;
        this.updatedAt = new Date();
    }

    /**
     * 拒绝好友请求
     */
    public void rejectFriendRequest() {
        this.status = 2;
        this.updatedAt = new Date();
    }

    /**
     * 检查好友关系是否已通过
     * @return 好友关系是否已通过
     */
    public boolean isFriendshipEstablished() {
        return this.status == 1;
    }

    /**
     * 检查是否是待验证状态
     * @return 是否是待验证状态
     */
    public boolean isPending() {
        return this.status == 0;
    }
}