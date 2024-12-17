package com.scm.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.scm.repsitories.UserRepo;

@Service
public class SecurityCustomUserDetailService implements UserDetailsService {

    
    @Autowired
    //user load from db
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       
        //we want to load user from db
        //check the username in db if not throw error msg
        return userRepo.findByEmail(username)
            .orElseThrow(()-> new UsernameNotFoundException("User not found with email : " +username));

    }

    

    


}
