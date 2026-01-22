package com.linkchat.infrastructure.persistence.entity;

import lombok.Data;

import java.util.Date;

@Data
public class UserEntity {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String avatar;
    private String email;
    private String phone;
    private Integer status;
    private Date createdAt;
    private Date updatedAt;
}