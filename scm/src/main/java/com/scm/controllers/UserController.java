package com.scm.controllers;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.entities.User;
import com.scm.helper.Hepler;
import com.scm.services.UserService;






//used for protect the url related to user
@Controller
@RequestMapping("/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

   

    //user dashbord page

    @RequestMapping(value = "/dashbaord")
    public String userDashbaord() {
        return "user/dashbaord";
    }
      
    //user profile page

    @RequestMapping(value = "/profile")
    public String userProfile(Model model, Authentication authentication) {
        
        

        return "user/profile";
    }

    //user add contact page

    //user view contacts

    //user edit contact

    //user delete contact

    //user search contact
    
    

}
