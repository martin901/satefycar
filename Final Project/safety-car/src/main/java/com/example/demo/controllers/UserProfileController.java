package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/profile")
public class UserProfileController {
    private UserService userService;

    @Autowired
    public UserProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showProfile(Model model) {
        User user = userService.getCurrentUser();
        model.addAttribute("user", user);
        return "userprofile";
    }

    @GetMapping("/edit")
    public String showEditProfile(Model model) {
        User user = userService.getCurrentUser();
        model.addAttribute("user", user);
        return "editprofile";
    }

    @PutMapping("/edit")
    public String editProfile(@Valid @ModelAttribute User user) {
        userService.updateById(user.getId(), user);
        return "redirect:/profile";
    }


}
