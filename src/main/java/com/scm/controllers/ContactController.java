package com.scm.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/contact")
public class ContactController {

    // add contact page
    @RequestMapping("/add")
    public String addContact(){
        return "user/add_contact";
    }
}
