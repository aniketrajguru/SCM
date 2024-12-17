package com.scm.helper;



import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import org.springframework.security.oauth2.core.user.OAuth2User;


public class Hepler {


    public static String getEmailOfLoggedInUser(Authentication authentication){

        
       
        
        //if user login with email and pass the how to get email: for fetching data from db
        if(authentication instanceof OAuth2AuthenticationToken){

            var aOAuth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;
            var clientid = aOAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

            var oauth2User = (OAuth2User)authentication.getPrincipal();
            String username="";

            if(clientid.equalsIgnoreCase("google")){
                //sign with google
                System.out.println("Getting email from google");
                username=oauth2User.getAttribute("email").toString();

            }
            else if(clientid.equalsIgnoreCase("github")){
                //sign with github 
                System.out.println("Getting email from github");
                username = oauth2User.getAttribute("email") != null ?
                oauth2User.getAttribute("email").toString() : oauth2User.getAttribute("login").toString()+"@gmail.com";

            }

            return username;
             

        //sign with github 
        }
        else{
            System.out.println("Getting data form local database");
            return authentication.getName();

        }
        
    }

}