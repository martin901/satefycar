package com.example.demo.controllers;

import com.example.demo.models.Enums.RequestStatus;
import com.example.demo.models.PolicyRequest;
import com.example.demo.models.User;
import com.example.demo.models.utils.pdf.PdfView;
import com.example.demo.services.PolicyRequestService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/requests")
public class UserRequestsController {
    private PolicyRequestService policyRequestService;

    private UserService userService;

    @Autowired
    public UserRequestsController(PolicyRequestService policyRequestService, UserService userService) {
        this.policyRequestService = policyRequestService;
        this.userService = userService;
    }

    @GetMapping
    public String showRequests(Model model, @PageableDefault(size = 8) Pageable pageable){
        User user = userService.getCurrentUser();
        model.addAttribute("user", user);

        Page<PolicyRequest> requests = policyRequestService.getAllByUser(user, pageable);
        PageWrapper<PolicyRequest> page = new PageWrapper<>(requests, "/requests");

        model.addAttribute("approved", RequestStatus.APPROVED);
        model.addAttribute("pending", RequestStatus.PENDING);
        model.addAttribute("declined", RequestStatus.DECLINED);
        model.addAttribute("canceled", RequestStatus.CANCELED);
        model.addAttribute("requests", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("size", requests.getTotalElements());

        return "requests";
    }

    @RequestMapping(value = "/{requestId}/pdf", method = RequestMethod.GET)
    public ModelAndView generatePdf(@PathVariable Long requestId) {
        checkRequestOwnership(requestId);
        PolicyRequest policyRequest = policyRequestService.getById(requestId);
        return new ModelAndView(new PdfView(), "policyrequest", policyRequest);
    }

    @GetMapping("/{requestId}/img")
    public void showImage(@PathVariable Long requestId, HttpServletResponse response) throws IOException {
        checkRequestOwnership(requestId);
        PolicyRequest policyRequest = policyRequestService.getById(requestId);
        response.setContentType("image/jpeg, image/jpg, image/png");
        response.getOutputStream().write(policyRequest.getImage().getImage());
        response.getOutputStream().close();
    }

    @RequestMapping(value="/{requestId}", method= RequestMethod.PUT, params="status=canceled")
    public String cancelRequest(@PathVariable Long requestId) {
        checkRequestOwnership(requestId);
        policyRequestService.cancelRequestById(requestId);
        return "redirect:/requests";
    }

    private void checkRequestOwnership(Long requestId) {
        User user = userService.getCurrentUser();
        PolicyRequest policyRequest;
        try {
            policyRequest = policyRequestService.getById(requestId);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        if (!policyRequest.getUser().equals(user)) {
            if (!user.isAdmin()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not Authorized");
            }
        }
    }
}
