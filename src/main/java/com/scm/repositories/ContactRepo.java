package com.scm.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.scm.entities.Contact;
import com.scm.entities.User;

public interface ContactRepo extends JpaRepository<Contact,String>{
    // find contacts by user
    // custom finder method
    List<Contact> findByUser(User user);

    // custom query method
    @Query("SELECT c FROM CONTACT c WHERE c.user.id = :userId")
    List<Contact> findByUserId(@Param("userId") String userId);
}
