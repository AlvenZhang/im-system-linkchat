package com.linkchat.infrastructure.persistence.entity;

import lombok.Data;

import java.util.Date;

@Data
public class GroupEntity {
    private Long id;
    private String name;
    private String avatar;
    private String description;
    private Long creatorId;
    private Integer memberCount;
    private Date createdAt;
    private Date updatedAt;
}