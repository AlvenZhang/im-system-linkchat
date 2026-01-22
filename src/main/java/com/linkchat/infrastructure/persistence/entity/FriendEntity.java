package com.linkchat.infrastructure.persistence.entity;

import lombok.Data;

import java.util.Date;

@Data
public class FriendEntity {
    private Long id;
    private Long userId;
    private Long friendId;
    private Integer status;
    private Date createdAt;
    private Date updatedAt;
}