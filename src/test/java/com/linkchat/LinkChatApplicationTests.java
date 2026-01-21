package com.linkchat;

import com.linkchat.entity.User;
import com.linkchat.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LinkChatApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    void testRegisterUser() {
        // 创建测试用户
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setNickname("测试用户");
        user.setEmail("test@example.com");
        
        // 注册用户
        boolean result = userService.registerUser(user);
        System.out.println("注册结果: " + result);
        
        // 验证用户是否注册成功
        User registeredUser = userService.getUserByUsername("testuser");
        System.out.println("注册的用户: " + registeredUser);
    }

}
