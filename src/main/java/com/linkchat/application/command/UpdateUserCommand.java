package com.linkchat.application.command;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserCommand {
    private Long id;
    private String nickname;
    private String avatar;
    private String phone;
    private Integer status;
}