package com.scm.config;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.util.List;
import com.scm.entities.Providers;
import com.scm.entities.User;
import com.scm.helper.AppConstants;
import com.scm.repsitories.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//this class use to go on user/profile after singin with google
@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    @Autowired
    private UserRepo userRepo;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        logger.info("OAuthAuthenticationSussessHandler");

        //identify the provider

        var oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        String authorizedClientRegistrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();

        logger.info(authorizedClientRegistrationId);

        var oauthUser = (DefaultOAuth2User)authentication.getPrincipal();

        oauthUser.getAttributes().forEach((key, value)->{
            logger.info(key + " : " + value);
        });

        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setRoleList(List.of(AppConstants.ROLE_USER));
        user.setEmailVerified(true);
        user.setEnabled(true);
        user.setPassword("Dummy");

        if(authorizedClientRegistrationId.equalsIgnoreCase("google")){

        //google
        //google attributes

        user.setEmail(oauthUser.getAttribute("email").toString());
        user.setProfilePic(oauthUser.getAttribute("picture").toString());
        user.setName(oauthUser.getAttribute("name").toString());
        user.setProviderUserId(oauthUser.getName());
        user.setProvider(Providers.GOOGLE);
        user.setAbout("This login is from Google");

        }
        else if(authorizedClientRegistrationId.equalsIgnoreCase("github")){
            //github
            //github attributes

            String email = oauthUser.getAttribute("email") != null ?
            oauthUser.getAttribute("email").toString() : oauthUser.getAttribute("login").toString()+"@gmail.com";
            
            String picture = oauthUser.getAttribute("avatar_url").toString();
            String name = oauthUser.getAttribute("login").toString();
            String providerUserId = oauthUser.getName();

            user.setEmail(email);
            user.setProfilePic(picture);
            user.setName(name);
            user.setProviderUserId(providerUserId);
            user.setProvider(Providers.GITHIB);
            user.setAbout("This login is from Github");
        }
        else{
            logger.info("OAuth2AuthenticationSuccessHandeler : Unkonwn provider");
        }

        //save the user
        User user2 = userRepo.findByEmail(user.getEmail()).orElse(null);
        if(user2==null){
            
            userRepo.save(user);
        }


        /* 
        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();

        // logger.info(user.getName());

        // user.getAttributes().forEach((key, value) ->{
        //     logger.info("{} => {}", key, value);
        // });

        // logger.info(user.getAuthorities().toString());

        //save data in db before redirect

        String email = user.getAttribute("email").toString();
        String name = user.getAttribute("name").toString();
        String picture = user.getAttribute("picture").toString();

        // create user and save in database

        User user1 = new User();
        user1.setEmail(email);
        user1.setName(name);
        user1.setProfilePic(picture);
        user1.setPassword("password");
        user1.setUserId(UUID.randomUUID().toString());
        user1.setProvider(Providers.GOOGLE);
        user1.setEnabled(true);

        user1.setEmailVerified(true);
        user1.setProviderUserId(user.getName());
        user1.setRoleList(List.of(AppConstants.ROLE_USER));
        user1.setAbout("This account is created using google..");
        
        //check user present in db or not if not then save in db
        User user2 = userRepo.findByEmail(email).orElse(null);
        if(user2==null){
            
            userRepo.save(user1);
            logger.info("User saved: " +email);
        }

        */

        
        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
        
    }

}
