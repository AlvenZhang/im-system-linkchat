package com.linkchat.domain.group.entity;

import lombok.Getter;

import java.util.Date;

@Getter
public class Group {
    private Long id;
    private String name;
    private String avatar;
    private String description;
    private Long creatorId;
    private Integer memberCount;
    private Date createdAt;
    private Date updatedAt;

    private Group() {
    }

    public static class Builder {
        private Group group = new Group();

        public Builder id(Long id) {
            group.id = id;
            return this;
        }

        public Builder name(String name) {
            group.name = name;
            return this;
        }

        public Builder avatar(String avatar) {
            group.avatar = avatar;
            return this;
        }

        public Builder description(String description) {
            group.description = description;
            return this;
        }

        public Builder creatorId(Long creatorId) {
            group.creatorId = creatorId;
            return this;
        }

        public Builder memberCount(Integer memberCount) {
            group.memberCount = memberCount;
            return this;
        }

        public Builder createdAt(Date createdAt) {
            group.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(Date updatedAt) {
            group.updatedAt = updatedAt;
            return this;
        }

        public Group build() {
            return group;
        }
    }

    /**
     * 更新群组信息
     * @param name 群组名称
     * @param avatar 群组头像URL
     * @param description 群组描述
     */
    public void updateGroupInfo(String name, String avatar, String description) {
        this.name = name;
        this.avatar = avatar;
        this.description = description;
        this.updatedAt = new Date();
    }

    /**
     * 增加群成员数量
     */
    public void incrementMemberCount() {
        this.memberCount++;
        this.updatedAt = new Date();
    }

    /**
     * 减少群成员数量
     */
    public void decrementMemberCount() {
        if (this.memberCount > 0) {
            this.memberCount--;
            this.updatedAt = new Date();
        }
    }

    /**
     * 检查用户是否是群主
     * @param userId 用户ID
     * @return 是否是群主
     */
    public boolean isCreator(Long userId) {
        return this.creatorId.equals(userId);
    }
}