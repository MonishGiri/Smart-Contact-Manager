package com.scm.controllers;

import java.util.UUID;

import org.slf4j.Logger ;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.JmsProperties.Listener.Session;
import org.springframework.data.domain.Page;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.forms.ContactSearchForm;
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
        model.addAttribute("contactSearchForm", new ContactSearchForm());
        
        return "user/contacts";
    }

    // search handler

    @GetMapping("/search")
    public String searchHandler(
        @ModelAttribute ContactSearchForm contactSearchForm,
        @RequestParam(value="size", defaultValue = AppConstants.PAGE_SIZE+"") int size,
        @RequestParam(value="page", defaultValue = "0") int page,
        @RequestParam(value="sortBy", defaultValue = "name") String sortBy,
        @RequestParam(value="direction", defaultValue = "asc") String order,
        Model model,
        Authentication authentication
    ){
        logger.info("field {} keyword {}",contactSearchForm.getField(), contactSearchForm.getKeyword());

        System.out.println("Keyword is: "+contactSearchForm.getKeyword());

        var user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));

        Page<Contact> pageContact = null;
        if(contactSearchForm.getField().equalsIgnoreCase("name")){
            pageContact = contactService.searchByName(contactSearchForm.getKeyword(), size, page, sortBy, order, user);
        }
        else if(contactSearchForm.getField().equalsIgnoreCase("email")){
            pageContact = contactService.searchByEmail(contactSearchForm.getKeyword(), size, page, sortBy, order, user);
        }
        else if(contactSearchForm.getField().equalsIgnoreCase("phoneNumber")){
            pageContact = contactService.searchByPhoneNumber(contactSearchForm.getKeyword(), size, page, sortBy, order, user);
        }

        for (Contact contact : pageContact) {
            System.out.print(contact+" ");
        }
        logger.info("Field: {}, Keyword: {}", contactSearchForm.getField(), contactSearchForm.getKeyword());
        model.addAttribute("pageContact", pageContact);
        model.addAttribute("contactSearchForm", contactSearchForm);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        logger.info("pageContact {}", pageContact);
        return "user/search";
    }

    @GetMapping("delete/{contactId}")
    public String deleteContact(@PathVariable String contactId, HttpSession session){
        System.out.println("Hello inside delete method");
        contactService.deleteContact(contactId);
        logger.info("contactId {} deleted",contactId);
        
        session.setAttribute("message", Message.builder().content("Contact Deleted Successfully").type(MessageType.green).build());
        return "redirect:/user/contact";
    }

    // update contact form view
    @GetMapping("/view/{contactId}")
    public String updateContactFormView(
        @PathVariable String contactId,
        Model model
    ){
        var contact = contactService.getContactById(contactId);
        
        ContactForm contactForm =  new ContactForm();
        contactForm.setName(contact.getName());
        contactForm.setAddress(contact.getAddress());
        contactForm.setEmail(contact.getEmail());
        contactForm.setDescription(contact.getDescription());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setFavorite(contact.isFavorite());
        contactForm.setWebsiteLink(contact.getWebsiteLink());
        contactForm.setLinkedInLink(contact.getLinkedInLink());
        contactForm.setPicture(contact.getPicture());
        System.out.println("Contact Image: "+contact.getPicture());
        System.out.println("contact form obj "+contactForm);
        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId", contactId);

        model.addAttribute("contactForm", contactForm);
        return "user/update_contact_view";
    }

    @PostMapping("/update/{contactId}")
    public String updateContact(@PathVariable("contactId") String contactId, @Valid @ModelAttribute ContactForm contactForm, BindingResult bindingResult , Model model, HttpSession session){

        // update the contact
        if (bindingResult.hasErrors()) {
            return "user/update_contact_view";
        }

        var con = contactService.getContactById(contactId);
        con.setId(contactId);
        con.setName(contactForm.getName());
        con.setAddress(contactForm.getAddress());
        con.setPhoneNumber(contactForm.getPhoneNumber());
        con.setEmail(contactForm.getEmail());
        con.setDescription(contactForm.getDescription());
        con.setFavorite(contactForm.isFavorite());
        con.setLinkedInLink(contactForm.getLinkedInLink());
        con.setWebsiteLink(contactForm.getWebsiteLink());

        // Process Image

        if(contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()){
            logger.info("file is not empty ");
        String fileName = UUID.randomUUID().toString();
        String imageUrl = imageService.uploadImage(contactForm.getContactImage(), fileName);
        con.setCloudinaryImagePublicId(fileName);
        con.setPicture(imageUrl);
        contactForm.setPicture(imageUrl);
        }
        else{
            logger.info("file is not empty");
        }

        var updatedCon = contactService.update(con);
        System.out.println("updated Contact "+updatedCon);
        session.setAttribute("message",  Message.builder()
        .content("Contact Updated Sucessfully")
        .type(MessageType.green)
        .build());
        
        return "redirect:/user/contact/view/"+contactId;
    }

}
