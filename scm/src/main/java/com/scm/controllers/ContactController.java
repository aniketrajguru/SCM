package com.scm.controllers;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.helper.AppConstants;
import com.scm.helper.Hepler; // Correct import statement
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    private Logger logger = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private ContactService contactService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    @RequestMapping("/add")
    public String addContactView(Model model) {
        ContactForm contactForm = new ContactForm();
        model.addAttribute("contactForm", contactForm);
        return "user/add_contact";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult result,
                              Authentication authentication, HttpSession session) {

        if (result.hasErrors()) {
            session.setAttribute("message", Message.builder()
                    .content("Please correct the following errors")
                    .type(MessageType.red)
                    .build());
            return "user/add_contact";
        }

        String username = Hepler.getEmailOfLoggedInUser(authentication); // Correct usage

        User user = userService.getUserByEmail(username);

        String filename = UUID.randomUUID().toString();
        String fileURL = imageService.uploadImage(contactForm.getContactImage(), filename);

        Contact contact = new Contact();
        contact.setName(contactForm.getName());
        contact.setFavorite(contactForm.isFavorite());
        contact.setEamil(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setUser(user);
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setPicture(fileURL);
        contact.setCloudinaryImagePublicId(filename);
        contactService.save(contact);

        session.setAttribute("message", Message.builder()
                .content("You have successfully added a new contact")
                .type(MessageType.green)
                .build());
        return "redirect:/user/contacts/add";
    }

    //view contacts

    @RequestMapping
    public String viewContacts(
        @RequestParam(value = "page",defaultValue = "0") int page,
        @RequestParam(value = "size",defaultValue = AppConstants.PAGE_SIZE + " " ) int size,
        @RequestParam(value= "sortBy",defaultValue = "name") String sortBy,
        @RequestParam(value = "direction",defaultValue = "asc") String direction,
        Model model, Authentication authentication) {
        String username = Hepler.getEmailOfLoggedInUser(authentication); // Correct usage

        User user = userService.getUserByEmail(username);

        Page<Contact> pageContact = contactService.getByUser(user,page,size,sortBy,direction);

        model.addAttribute("pageContact", pageContact);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

        return "user/contacts";
    }

    @RequestMapping("/search")
    public String searchHandler(
        @RequestParam("field") String field,
        @RequestParam("keyword") String value,
        @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
        @RequestParam(value="page", defaultValue = "0") int page,
        @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
        @RequestParam(value = "direction", defaultValue = "asc") String direction, 
        Model model
        ){

            logger.info("field {} keyword {}", field, value);


            Page<Contact> pageContact=null;
            if(field.equalsIgnoreCase("name")){
                pageContact = contactService.searchByName(value, size, page, sortBy, direction);
            }
            else if(field.equalsIgnoreCase("email")){
                pageContact = contactService.searchByEmail(value, size, page, sortBy, direction);
            }
            else if(field.equalsIgnoreCase("phoneNumber")){
                pageContact = contactService.searchByPhoneNumber(value, size, page, sortBy, direction);
            }

            logger.info("pageContact {}", pageContact);

            model.addAttribute("pageContact", pageContact);


            return "user/search";
        }

        //delete contact
        @RequestMapping("/delete/{contactId}")
        public String deleteContact(
            @PathVariable("contactId") String contactId){
                contactService.delete(contactId);
                logger.info("contactId {} deleted", contactId);

                return "redirect:/user/contacts";
            }
        
        //update contact form view
        @GetMapping("/view/{contactId}")
        public String updateContactFormView(
            @PathVariable("contactId") String contactId,
            Model model){
                
                var contact = contactService.getById(contactId);

                ContactForm contactForm = new ContactForm();

                contactForm.setName(contact.getName());
                contactForm.setEmail(contact.getEamil());
                contactForm.setPhoneNumber(contact.getPhoneNumber());
                contactForm.setAddress(contact.getAddress());
                contactForm.setDescription(contact.getDescription());
                contactForm.setFavorite(contact.isFavorite());
                contactForm.setWebsiteLink(contact.getWebsiteLink());
                contactForm.setLinkedInLink(contact.getLinkedInLink());
                
                model.addAttribute("contactForm", contactForm);
                model.addAttribute("contactId", contactId);

                return "user/update_contact_view";
            }


            @RequestMapping(value = "/update/{contactId}", method = RequestMethod.POST)
            public String updateContact(@PathVariable("contactId") String contactId, @ModelAttribute ContactForm contactForm,
                        Model model){

                            //update the contact

                            var con = new Contact();
                            con.setId(contactId);
                            con.setName(contactForm.getName());
                            con.setEamil(contactForm.getEmail());
                            con.setPhoneNumber(contactForm.getPhoneNumber());
                            con.setAddress(contactForm.getAddress());
                            con.setDescription(contactForm.getDescription());
                            con.setFavorite(contactForm.isFavorite());
                            con.setWebsiteLink(contactForm.getWebsiteLink());
                            con.setLinkedInLink(contactForm.getLinkedInLink());
                            


                            var updateCon = contactService.update(con);
                            logger.info("updated contact {}", updateCon);
                            model.addAttribute("message", Message.builder()
                            .content("Contact Updated")
                            .type(MessageType.green) // Use the enum value directly
                            .build());
                                                    
                            return "redirect:/user/contacts/view" + contactId;
                        }

}
