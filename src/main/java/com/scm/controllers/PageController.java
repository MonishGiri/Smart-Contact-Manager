package com.scm.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class PageController {

    @RequestMapping("/home")
    public String home(Model model){
        System.out.println("Home Page Handler");
        model.addAttribute("name","Substring Technologies");
        model.addAttribute("MyName","Monish Giri");

        return "home";
    }

    // about route
    @GetMapping("/about")
    public String aboutPage(Model model) {
        System.out.println("About page loading");
        model.addAttribute("isLogin",false);
        return "about";
    }

    // servce route
    @GetMapping("/services")
    public String servicesPage() {
        System.out.println("About page loading");
        return "services";
    }

    // contact route
    @GetMapping("/contact")
    public String contactPage() {
        System.out.println("contact page loading");
        return "contact";
    }

    // Login route
    @GetMapping("/login")
    public String loginPage() {
        System.out.println("Login page loading");
        return "login";
    }

    // servce route
    @GetMapping("/register")
    public String registerPage() {
        System.out.println("Register page loading");
        return "register";
    }
    
}
