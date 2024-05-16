package com.scm.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @RequestMapping("/home")
    public String home(Model model){
        System.out.println("Home Page Handler");
        model.addAttribute("name","Substring Technologies");
        model.addAttribute("MyName","Monish Giri");

        return "home";
    }
}
