package com.scm.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.scm.entities.Contact;
import com.scm.entities.User;

public interface ContactService {
    
    //save contact
    Contact save(Contact contact);

    //update contact
    Contact update(Contact contact);

    //get contacts
    List<Contact> getAll();

    //get contact by id
    Contact getById(String id);

    //delete contact by id
    void delete(String id);

    //search contact
    Page<Contact> searchByName(String nameKeyword, int size, int page, String sortBy, String order);

    Page<Contact> searchByEmail(String emailKeyword, int size, int page, String sortBy, String order);

    Page<Contact> searchByPhoneNumber(String phoneNumberKeyword, int size, int page, String sortBy, String order);


    //get contact by Userid
    List<Contact> getByUserId(String userid);

    Page<Contact> getByUser(User user, int page, int size, String sortField, String sortDirection );

}
