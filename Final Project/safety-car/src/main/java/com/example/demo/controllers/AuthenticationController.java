package com.example.demo.controllers;

import com.example.demo.models.ConfirmationToken;
import com.example.demo.models.User;
import com.example.demo.services.ConfirmationTokenService;
import com.example.demo.services.EmailSenderService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class AuthenticationController {
    private UserService userService;
    private ConfirmationTokenService confirmationTokenService;
    private EmailSenderService emailSenderService;

    @Autowired
    public AuthenticationController(UserService userService, ConfirmationTokenService confirmationTokenService, EmailSenderService emailSenderService) {
        this.userService = userService;
        this.emailSenderService = emailSenderService;
        this.confirmationTokenService = confirmationTokenService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute(new User());
        return "register";
    }

    @PostMapping("/register")
    public String createUser(@Valid @ModelAttribute(name = "user") User user, BindingResult result, Model model) {
        if (result.hasErrors() || hasUsernameOrEmailErrors(user, result)) {
            return "register";
        }
        try {
            userService.create(user, true);

            ConfirmationToken confirmationToken = new ConfirmationToken(user);

            confirmationTokenService.save(confirmationToken);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("safetycar.dev@gmail.com");
            mailMessage.setText("Hello " + user.getName().getFirstName() + ",\n\nTo confirm your account, please click here : "
                    + "http://localhost:8080/confirm-account?token=" + confirmationToken.getConfirmationToken() + "\n\nKind regards,\nSafety Car Team");

            emailSenderService.sendEmail(mailMessage);
            model.addAttribute("email", user.getEmail());

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return "successfulregistration";
    }

    private boolean hasUsernameOrEmailErrors(User user, BindingResult result){
        boolean hasErrors = false;
        if(userService.getByUsername(user.getUsername())!=null) {
            result.rejectValue("username", "error.username", "Username is already in use");
            hasErrors = true;
        }
        if(userService.getByEmail(user.getEmail())!=null) {
            result.rejectValue("email", "error.email", "Email is already in use");
            hasErrors = true;
        }
        return hasErrors;
    }

    @RequestMapping(value = "/confirm-account", method = {RequestMethod.GET, RequestMethod.POST})
    public String confirmUserAccount(Model model, @RequestParam("token") String confirmationToken) {
        ConfirmationToken token = confirmationTokenService.findByConfirmationToken(confirmationToken);

        if (token != null) {
            User user = userService.getByEmail(token.getUser().getEmail());
            user.setEnabled(true);
            userService.create(user, false);
            model.addAttribute("firstname", user.getName().getFirstName());
        }

        return "successfulactivation";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }

}
