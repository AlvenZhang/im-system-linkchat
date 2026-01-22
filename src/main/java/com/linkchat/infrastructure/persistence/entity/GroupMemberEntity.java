package com.linkchat.infrastructure.persistence.entity;

import lombok.Data;

import java.util.Date;

@Data
public class GroupMemberEntity {
    private Long id;
    private Long groupId;
    private Long userId;
    private Integer role;
    private Date joinedAt;
    private Date updatedAt;
}