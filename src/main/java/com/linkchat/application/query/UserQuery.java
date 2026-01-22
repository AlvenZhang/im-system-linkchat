package com.linkchat.application.query;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserQuery {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private Integer status;
}