package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.User;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.repositories.UserRepo;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepo userRepo;

    // verify email
    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token, HttpSession session){
        System.out.println("hello verify email");

        User user = userRepo.findByEmailToken(token).orElse(null);
        if(user == null) System.out.println("user is null");
        if(user != null){
            // user if fetched, Now process 
            if(user.getEmailToken().equals(token)){
                user.setEmailVerified(true);
                user.setEnabled(true);
                userRepo.save(user);
                session.setAttribute("message", Message.builder()
        .type(MessageType.green)
        .content("Email Verified, Now you can Login").build());
                return "user/sucess_page";
            }
            session.setAttribute("message", Message.builder()
        .type(MessageType.red)
        .content("Email not verified, Something went wrong").build());
            return "user/error_page";

        }
        session.setAttribute("message", Message.builder()
        .type(MessageType.red)
        .content("Email not verified, Something went wrong").build());
        return "user/error_page";

    }
}
