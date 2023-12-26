package com.employeedir.demo.controller;

import com.employeedir.demo.chat.model.ChatUser;
import com.employeedir.demo.model.User;
import com.employeedir.demo.securityservice.UserService;
import com.employeedir.demo.util.RedirectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.Objects;

@Controller
public class ImageController {

    @Autowired
    private UserService userService;


    @PostMapping("/updateProfileImage")
    public String updateProfileImage(@RequestParam("fileImage") MultipartFile fileImage, Principal principal, HttpServletRequest request) throws IOException {


        String url = request.getHeader("referer");

        String endpoint = RedirectUtil.extractEndpoint(url);


        String fileName = StringUtils.cleanPath(Objects.requireNonNull(fileImage.getOriginalFilename()));


        if (fileName.isEmpty()) {
            return "redirect:" + endpoint;
        }

        User user = userService.findUserByName(principal.getName());

        user.setImage(fileName);
        user.setImageData(fileImage.getBytes());


        userService.updateUser(user);




        return "redirect:" + endpoint;

    }


}
