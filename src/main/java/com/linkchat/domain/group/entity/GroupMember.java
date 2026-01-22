package com.linkchat.domain.group.entity;

import lombok.Getter;

import java.util.Date;

@Getter
public class GroupMember {
    private Long id;
    private Long groupId;
    private Long userId;
    private Integer role;
    private Date joinedAt;
    private Date updatedAt;

    private GroupMember() {
    }

    public static class Builder {
        private GroupMember groupMember = new GroupMember();

        public Builder id(Long id) {
            groupMember.id = id;
            return this;
        }

        public Builder groupId(Long groupId) {
            groupMember.groupId = groupId;
            return this;
        }

        public Builder userId(Long userId) {
            groupMember.userId = userId;
            return this;
        }

        public Builder role(Integer role) {
            groupMember.role = role;
            return this;
        }

        public Builder joinedAt(Date joinedAt) {
            groupMember.joinedAt = joinedAt;
            return this;
        }

        public Builder updatedAt(Date updatedAt) {
            groupMember.updatedAt = updatedAt;
            return this;
        }

        public GroupMember build() {
            return groupMember;
        }
    }

    /**
     * 将成员设为管理员
     */
    public void promoteToAdmin() {
        this.role = 1;
        this.updatedAt = new Date();
    }

    /**
     * 将管理员设为普通成员
     */
    public void demoteToMember() {
        this.role = 0;
        this.updatedAt = new Date();
    }

    /**
     * 检查是否是群主
     * @return 是否是群主
     */
    public boolean isCreator() {
        return this.role == 2;
    }

    /**
     * 检查是否是管理员
     * @return 是否是管理员
     */
    public boolean isAdmin() {
        return this.role == 1;
    }

    /**
     * 检查是否是普通成员
     * @return 是否是普通成员
     */
    public boolean isNormalMember() {
        return this.role == 0;
    }
}