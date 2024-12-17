package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;







@Controller
public class PageController {
    

    @Autowired
    private UserService userService;


    @GetMapping("/")
    public String index(){
        return "redirect:/home";
    }


    @RequestMapping("/home")
    public String home(Model model){

        //sending data to view
        model.addAttribute("name", "aniket");
        model.addAttribute("youtube", "learn");
        return "home";
    }

    //about page
    @RequestMapping("/about")
    public String aboutPage(){
        System.out.println("about page loading");
        return "about";
    }

    //services

    @RequestMapping("/services")
    public String servicesPage(){
        System.out.println("services page loading");
        return "services";
    }

    //contact page

    @GetMapping("/contact")
    public String contact() {
        return new String("contact");
    }

    //this is showing login page
    @GetMapping("/login")
    public String login() {
        return new String("login");
    }

    //registration page
    @GetMapping("/register")
    public String register(Model model) {
       
        UserForm userForm = new UserForm();
        //set default name
        // userForm.setName("Aniket");
        // userForm.setAbout("write something about you...");
        model.addAttribute("userForm", userForm);


        return "register";
    }

    //processing registration

    @RequestMapping(value = "/do-register",method = RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult,  HttpSession session){
        System.out.println("processing registration");
        //fetch form data
        System.out.println(userForm);
        //validate form data
        if(rBindingResult.hasErrors()){
            return "register";
        }
        //save to database
        //userservice

        //convert userForm to User
        // User user = User.builder()
        //         .name(userForm.getName())
        //         .email(userForm.getEmail())
        //         .password(userForm.getPassword())
        //         .about(userForm.getAbout())
        //         .phoneNumber(userForm.getPhoneNumber())
        //         .profilePic("https://t3.ftcdn.net/jpg/00/64/67/52/360_F_64675209_7ve2XQANuzuHjMZXP3aIYIpsDKEbF5dD.jpg")
        //         .build();
        
        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setProfilePic("https://t3.ftcdn.net/jpg/00/64/67/52/360_F_64675209_7ve2XQANuzuHjMZXP3aIYIpsDKEbF5dD.jpg");       

        User saveUser = userService.saveUser(user);

        System.out.println("user save :");
        //message="regisration successful"

        //add the message using HttpSession

        Message message =Message.builder().content("Registration Successful").type(MessageType.green).build();

        session.setAttribute("message", message);

        //redirect login page
        return "redirect:/register";
    }
    
    
    

}
