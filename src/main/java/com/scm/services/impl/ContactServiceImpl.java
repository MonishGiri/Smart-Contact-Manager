package com.scm.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.helper.ResourceNotFoundException;
import com.scm.repositories.ContactRepo;
import com.scm.services.ContactService;

@Service
public class ContactServiceImpl implements ContactService{

    @Autowired
    private ContactRepo contactRepo;
    @Override
    public Contact save(Contact contact) {
        String contactId = UUID.randomUUID().toString();
        contact.setId(contactId);
        return contactRepo.save(contact);
    }

    @Override
    public Contact update(Contact contact) {
        var oldContact = contactRepo.findById(contact.getId()).orElseThrow(() -> new ResourceNotFoundException("Contact Not found"));
        System.out.println("Old contact : "+oldContact);
        oldContact.setName(contact.getName());
        oldContact.setEmail(contact.getEmail());
        oldContact.setAddress(contact.getAddress());
        oldContact.setFavorite(contact.isFavorite());
        oldContact.setDescription(contact.getDescription());
        oldContact.setPhoneNumber(contact.getPhoneNumber());
        oldContact.setPicture(contact.getPicture());
        oldContact.setWebsiteLink(contact.getWebsiteLink());
        oldContact.setLinkedInLink(contact.getLinkedInLink());
        oldContact.setCloudinaryImagePublicId(contact.getCloudinaryImagePublicId());

        System.out.println("Picture is: "+oldContact.getPicture());
        return contactRepo.save(oldContact);
    }

    @Override
    public List<Contact> getAllContacts() {
        return contactRepo.findAll();
    }

    @Override
    public Contact getContactById(String id) {
        return contactRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact not found with given id "+id));
    }

    @Override
    public void deleteContact(String id) {
        var contact = contactRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact not found with given id "+id));
        contactRepo.delete(contact);
    }


    @Override
    public List<Contact> getByUserId(String userId) {
        return contactRepo.findByUserId(userId);
    }

    @Override
    public Page<Contact> getByUser(User user, int page, int size, String sortyBy, String sortDirection) {
        Sort sort = sortDirection.equals("desc") ? Sort.by(sortyBy).descending() : Sort.by(sortyBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByUser(user, pageable);
    }

    @Override
    public Page<Contact> searchByName(String nameKeyword, int size, int page, String sortBy, String order, User user) {
        Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByUserAndNameContaining(user, nameKeyword, pageable);
    }

    @Override
    public Page<Contact> searchByEmail(String emailKeyword, int size, int page, String sortBy, String order, User user) {
        Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByUserAndEmailContaining(user, emailKeyword, pageable);
    }

    @Override
    public Page<Contact> searchByPhoneNumber(String phoneNumberKeyword, int size, int page, String sortBy,
            String order, User user) {
                Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
                var pageable = PageRequest.of(page, size, sort);
                return contactRepo.findByUserAndPhoneNumberContaining(user, phoneNumberKeyword, pageable);
    }

    

}
