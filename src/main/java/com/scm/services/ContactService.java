package com.scm.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.scm.entities.Contact;
import com.scm.entities.User;

@Service
public interface ContactService {
    
    // save contacts
    Contact save(Contact contact);

    // update contact
    Contact update(Contact contact);

    // get contacts
    List<Contact> getAllContacts();

    // get contact by id
    Contact getContactById(String id);

    // delete contact
    void deleteContact(String id);

    // search contact
    List<Contact> searchContact(String email, String name, String phoneNumber);

    // get contacts by user id
    List<Contact> getByUserId(String userId);

    Page<Contact> getByUser(User user, int page, int size, String sortyBy, String sortDirection);

}
