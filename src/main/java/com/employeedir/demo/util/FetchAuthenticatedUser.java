package com.employeedir.demo.util;


import com.employeedir.demo.chat.model.ChatUser;
import com.employeedir.demo.model.User;
import com.employeedir.demo.securityservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class FetchAuthenticatedUser {

    @Autowired
    private UserService userService;

    public ChatUser getUser() {

        Principal principal = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.findUserByName(principal.getName());

        ChatUser currentUser = new ChatUser(user.getUserName(), user.getImage(), user.getImageData());

        return currentUser;


    }


}
