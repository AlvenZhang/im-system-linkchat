package com.linkchat.infrastructure.persistence.entity;

import lombok.Data;

import java.util.Date;

@Data
public class MessageEntity {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private Integer type;
    private String content;
    private Integer status;
    private Boolean isGroup;
    private Date createdAt;
}