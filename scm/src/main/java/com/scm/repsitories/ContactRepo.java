package com.scm.repsitories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.scm.entities.Contact;
import com.scm.entities.User;

import java.util.List;


@Repository
public interface ContactRepo extends JpaRepository<Contact, String>{
    //find the contact by user
    //custom finder method
    Page<Contact> findByUser(User user, Pageable pageable);

    //custom query method
    @Query("SELECT c FROM Contact c WHERE c.user.id = :userId")
    List<Contact> findByUserId(@Param("userId") String userId);

    Page<Contact> findByNameContaining(String nameKeyword, Pageable pageable);
    Page<Contact> findByEamilContaining(String emailKeyword, Pageable pageable);
    Page<Contact> findByPhoneNumberContaining(String phoneKeyword, Pageable pageable);

}