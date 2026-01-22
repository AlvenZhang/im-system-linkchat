package com.linkchat.application.command;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserCommand {
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String phone;
}