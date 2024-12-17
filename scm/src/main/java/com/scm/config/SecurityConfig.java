package com.scm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.scm.services.impl.SecurityCustomUserDetailService;

@Configuration
public class SecurityConfig {

    //user create and login using java code with in memory services

    // @Bean
    // public UserDetailsService userDetailsService(){

    //     UserDetails user1 = User
    //         .withDefaultPasswordEncoder()
    //         .username("admin123")
    //         .password("admin123")
    //         .roles("ADMIN","USER")
    //         .build();

    //      var inMemoryUserDetailsManager = new InMemoryUserDetailsManager(user1);
    //      return inMemoryUserDetailsManager;
    // }

    @Autowired
    private SecurityCustomUserDetailService userDetailService;

    @Autowired
    private OAuthAuthenticationSuccessHandler handler;    

    //configuration of authentication provider for spring security
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        //user detail services object
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        //password encoder object
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

        //configuration
        //url's are configure here for public and private
        httpSecurity.authorizeHttpRequests(authorize ->{ 
            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();
    });

        
        //if any changes required related to form login then use this
        httpSecurity.formLogin(formLogin -> {

            formLogin.loginPage("/login");
            //login form submit on authenticate
            formLogin.loginProcessingUrl("/authenticate");
            //after login just forward on
            formLogin.successForwardUrl("/user/profile");
            //if login feild
            // formLogin.failureForwardUrl("/login?error=true");
            formLogin.usernameParameter("email");
            formLogin.passwordParameter("password");

        });

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.logout(logoutForm ->{
            logoutForm.logoutUrl("/do-logout");
            logoutForm.logoutSuccessUrl("/login?logout=true");
        });

        //oauth configurations

        httpSecurity.oauth2Login(oauth ->{
            oauth.loginPage("/login");
            oauth.successHandler(handler);
        });

        return httpSecurity.build();
    }

    @Bean 
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
