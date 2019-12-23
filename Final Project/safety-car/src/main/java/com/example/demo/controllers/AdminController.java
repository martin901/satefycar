package com.example.demo.controllers;

import com.example.demo.models.Enums.RequestStatus;
import com.example.demo.models.MulticriteriaTable;
import com.example.demo.models.PolicyRequest;
import com.example.demo.models.TableChangeRecord;
import com.example.demo.models.User;
import com.example.demo.services.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private PolicyRequestService policyRequestService;
    private MulticriteriaTableService multicriteriaTableService;
    private UserService userService;
    private EmailSenderService emailSenderService;
    private TableChangeRecordService tableChangeRecordService;

    @Autowired
    public AdminController(PolicyRequestService policyRequestService, MulticriteriaTableService multicriteriaTableService, UserService userService, EmailSenderService emailSenderService, TableChangeRecordService tableChangeRecordService) {
        this.policyRequestService = policyRequestService;
        this.multicriteriaTableService = multicriteriaTableService;
        this.userService = userService;
        this.emailSenderService = emailSenderService;
        this.tableChangeRecordService = tableChangeRecordService;
    }

    @GetMapping("/requests")
    public String showPendingRequests(@RequestParam(name = "user", required = false) String user,
                                      @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
                                      @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
                                      @RequestParam(name = "status", required = false) RequestStatus status,
                                      Model model, @PageableDefault(size = 8) Pageable pageable) {
        Page<PolicyRequest> requests;
        try {
            requests = policyRequestService.get(user, fromDate, toDate, status, pageable);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        PageWrapper<PolicyRequest> page = new PageWrapper<>(requests, "/admin/requests");

        User userPrincipal = userService.getCurrentUser();

        model.addAttribute("requestsStatuses", RequestStatus.values());
        model.addAttribute("status", status);
        model.addAttribute("searchUser", user);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        model.addAttribute("user", userPrincipal);
        model.addAttribute("approved", RequestStatus.APPROVED);
        model.addAttribute("pending", RequestStatus.PENDING);
        model.addAttribute("declined", RequestStatus.DECLINED);
        model.addAttribute("expired", RequestStatus.EXPIRED);
        model.addAttribute("canceled", RequestStatus.CANCELED);
        model.addAttribute("requests", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("size", requests.getTotalElements());

        return "admin-requests";
    }

    @GetMapping("/upload")
    public String showUploadPage(Model model) {
        User user = userService.getCurrentUser();
        model.addAttribute("user", user);
        model.addAttribute("file", new MulticriteriaTable());
        model.addAttribute("multicriteriaTable", multicriteriaTableService.getAll());
        model.addAttribute("lastRecord", tableChangeRecordService.getLastRecord());

        return "admin-upload";
    }

    @PostMapping("/upload")
    public String uploadMulticriteriaFile(@RequestParam(name = "file") MultipartFile file) {
        User user = userService.getCurrentUser();
        try {
            multicriteriaTableService.save(file);
        } catch (IllegalArgumentException | IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        TableChangeRecord tableChangeRecord = new TableChangeRecord();
        tableChangeRecord.setUser(user);
        tableChangeRecordService.save(tableChangeRecord);
        return "redirect:/admin/upload";
    }

    @RequestMapping(value = "/requests/{requestId}", method = RequestMethod.PUT, params = "status=approved")
    public String approveRequest(@PathVariable Long requestId) {
        User user = userService.getCurrentUser();
        policyRequestService.approveRequestById(requestId, user);

        PolicyRequest request = policyRequestService.getById(requestId);
        User requester = request.getUser();
        SimpleMailMessage mailMessage = returnMessage(requester, "Approved", request);
        emailSenderService.sendEmail(mailMessage);
        return "redirect:/admin/requests";
    }

    @RequestMapping(value = "/requests/{requestId}", method = RequestMethod.PUT, params = "status=declined")
    public String declineRequest(@PathVariable Long requestId) {
        User user = userService.getCurrentUser();
        policyRequestService.declineRequestById(requestId, user);

        PolicyRequest request = policyRequestService.getById(requestId);
        User requester = request.getUser();
        SimpleMailMessage mailMessage = returnMessage(requester, "Declined", request);
        emailSenderService.sendEmail(mailMessage);
        return "redirect:/admin/requests";
    }

    private SimpleMailMessage returnMessage(User requester, String approvedOrDeclined, PolicyRequest request) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(requester.getEmail());
        mailMessage.setSubject(approvedOrDeclined + " car insurance policy!");
        mailMessage.setFrom("safetycar.dev@gmail.com");
        mailMessage.setText("Hello, " + requester.getName() + ",\n\nYour request for car " +
                request.getOffer().getCarModel().getCarBrand().getBrandName() + " " + request.getOffer().getCarModel().getModelName() +
                " you placed on " + dateFormat.format(request.getRequestDate()) + " has been " + approvedOrDeclined + "!\n" +
                "You can print your policy from here: http://localhost:8080/requests/" + request.getId() + "/pdf\n\n"
                + "Kind regards,\nSafety Car Team");

        return mailMessage;
    }
}
