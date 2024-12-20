package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.helper.Helper;
import com.scm.services.ContactService;
import com.scm.services.UserService;


@Controller
@RequestMapping("/user/contact")
public class ContactController {

    @Autowired
    ContactService contactService;

    @Autowired
    UserService userService;

    // add contact page
    @RequestMapping("/add")
    public String addContact(Model model){
        ContactForm contactForm = new ContactForm();

        model.addAttribute("contactForm",contactForm);
        return "user/add_contact";
    }

    @PostMapping("/add")
    public String saveContact(@ModelAttribute ContactForm contactForm, Authentication authentication){

        // process form data
        String userName = Helper.getEmailOfLoggedInUser(authentication);

        userService.getUserByEmail(userName);

        User user = userService.getUserByEmail(userName);
        Contact contact = new Contact();
        contact.setName(contactForm.getName());
        contact.setAddress(contactForm.getAddress());
        contact.setFavorite(contactForm.isFavorite());
        contact.setEmail(contactForm.getEmail());
        contact.setDescription(contactForm.getDescription());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setUser(user);
        contactService.save(contact);
        return "redirect:/user/contact/add";
    }
}
