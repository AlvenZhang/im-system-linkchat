package com.linkchat.domain.user.entity;

import com.linkchat.domain.user.value.Email;
import com.linkchat.domain.user.value.UserId;
import com.linkchat.domain.user.value.Username;
import lombok.Getter;

import java.util.Date;

@Getter
public class User {
    private UserId id;
    private Username username;
    private String password;
    private String nickname;
    private String avatar;
    private Email email;
    private String phone;
    private Integer status;
    private Date createdAt;
    private Date updatedAt;

    private User() {
    }

    public static class Builder {
        private User user = new User();

        public Builder id(Long id) {
            user.id = UserId.of(id);
            return this;
        }

        public Builder username(String username) {
            user.username = Username.of(username);
            return this;
        }

        public Builder password(String password) {
            user.password = password;
            return this;
        }

        public Builder nickname(String nickname) {
            user.nickname = nickname;
            return this;
        }

        public Builder avatar(String avatar) {
            user.avatar = avatar;
            return this;
        }

        public Builder email(String email) {
            user.email = Email.of(email);
            return this;
        }

        public Builder phone(String phone) {
            user.phone = phone;
            return this;
        }

        public Builder status(Integer status) {
            user.status = status;
            return this;
        }

        public Builder createdAt(Date createdAt) {
            user.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(Date updatedAt) {
            user.updatedAt = updatedAt;
            return this;
        }

        public User build() {
            return user;
        }
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
        this.updatedAt = new Date();
    }

    public void updateAvatar(String avatar) {
        this.avatar = avatar;
        this.updatedAt = new Date();
    }

    public void updatePhone(String phone) {
        this.phone = phone;
        this.updatedAt = new Date();
    }

    public void updatePassword(String password) {
        this.password = password;
        this.updatedAt = new Date();
    }

    public void updateStatus(Integer status) {
        this.status = status;
        this.updatedAt = new Date();
    }

    public void activate() {
        this.status = 1;
        this.updatedAt = new Date();
    }

    public void deactivate() {
        this.status = 0;
        this.updatedAt = new Date();
    }
}