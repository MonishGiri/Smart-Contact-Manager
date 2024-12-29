package com.scm.controllers;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger ;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.helper.AppConstants;
import com.scm.helper.Helper;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/user/contact")
public class ContactController {

    private Logger logger = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private ImageService imageService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    // add contact page
    @RequestMapping("/add")
    public String addContact(Model model){
        ContactForm contactForm = new ContactForm();

        model.addAttribute("contactForm",contactForm);
        return "user/add_contact";
    }

    @PostMapping("/add")
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult result,Authentication authentication, HttpSession session){

        // process form data

        // validate form

        if(result.hasErrors()){
            session.setAttribute("message", Message.builder()
            .content("Please correct the following errors")
            .type(MessageType.red)
            .build());
            return "user/add_contact";
        } 


        String userName = Helper.getEmailOfLoggedInUser(authentication);

        userService.getUserByEmail(userName);

        User user = userService.getUserByEmail(userName);

        String fileName = UUID.randomUUID().toString();
        // code for uploading the image
        String fileUrl = imageService.uploadImage(contactForm.getContactImage(), fileName);

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
        contact.setPicture(fileUrl);
        contact.setCloudinaryImagePublicId(fileName);
        contactService.save(contact);

        // set contact picture url

        // set message or display on the view
        session.setAttribute("message",  Message.builder()
        .content("New Contact Added Sucessfully")
        .type(MessageType.green)
        .build());
        return "redirect:/user/contact/add";
    }

    // view contacts
    @GetMapping
    public String viewContacts(
        @RequestParam(value = "page", defaultValue = "0") int page, 
        @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE+"") int size, 
        @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
        @RequestParam(value = "direction", defaultValue = "asc") String sortDirection,
      Authentication authentication, Model model){

        // load all the contacts of user
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);
        Page<Contact> pageContacts =  contactService.getByUser(user,page,size, sortBy, sortDirection);
        model.addAttribute("pageSize",AppConstants.PAGE_SIZE);
        model.addAttribute("pageContact",pageContacts);
        
        return "user/contacts";
    }

    // search handler

    @GetMapping("/search")
    public String searchHandler(
        @RequestParam("field") String field,
        @RequestParam("keyword") String keyword,
        @RequestParam(value="size", defaultValue = AppConstants.PAGE_SIZE+"") int size,
        @RequestParam(value="page", defaultValue = "0") int page,
        @RequestParam(value="sortBy", defaultValue = "name") String sortBy,
        @RequestParam(value="direction", defaultValue = "asc") String order,
        Model model
    ){
        logger.info("field {} keyword {}",field, keyword);

        Page<Contact> pageContact = null;
        if(field.equalsIgnoreCase("name")){
            pageContact = contactService.searchByName(keyword, size, page, sortBy, order);
        }
        else if(field.equalsIgnoreCase("email")){
            pageContact = contactService.searchByEmail(keyword, size, page, sortBy, order);
        }
        else if(field.equalsIgnoreCase("phoneNumber")){
            pageContact = contactService.searchByPhoneNumber(keyword, size, page, sortBy, order);
        }
        logger.info("Field: {}, Keyword: {}", field, keyword);
        model.addAttribute("pageContact", pageContact);
        logger.info("pageContact {}", pageContact);
        return "user/search";
    }
    
}
