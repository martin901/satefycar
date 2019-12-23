package com.example.demo.controllers;

import com.example.demo.models.CarModel;
import com.example.demo.models.Offer;
import com.example.demo.models.PolicyRequest;
import com.example.demo.models.User;
import com.example.demo.models.utils.PolicyCalculator;
import com.example.demo.services.PolicyRequestService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/offer")
public class OfferController {
    private UserService userService;
    private PolicyRequestService policyRequestService;

    @Autowired
    public OfferController(UserService userService, PolicyRequestService policyRequestService) {
        this.userService = userService;
        this.policyRequestService = policyRequestService;
    }

    @GetMapping
    public String showOfferPage(Model model, HttpSession session){
        helper(session, model);
        model.addAttribute("policyrequest", new PolicyRequest());
        return "offer";
    }

    @PostMapping
    public String getRequest(@Valid @ModelAttribute("policyrequest") PolicyRequest request, BindingResult result, @RequestParam("docimage") MultipartFile img, HttpSession session, Model model) {
        if(result.hasErrors()){
            helper(session, model);
            return "offer";
        }

        User user = userService.getCurrentUser();
        PolicyCalculator calculator = (PolicyCalculator) session.getAttribute("calculatorResult");
        Offer offer = calculator.getOffer();
        request.setUser(user);
        request.setOffer(offer);

        boolean isClosed = false;
        isClosed = policyRequestService.checkCurrentTime(isClosed, request);
        model.addAttribute("isClosed", isClosed);

        if(isClosed){
            helper(session, model);
            return "offer";
        }

        policyRequestService.create(request, img);
        return "redirect:/requests";
    }

    private void helper(HttpSession session, Model model) {
        PolicyCalculator policyCalculatorResult = (PolicyCalculator) session.getAttribute("calculatorResult");
        double totalPremium = (double) session.getAttribute("totalPremium");
        CarModel carModel = (CarModel) session.getAttribute("carmodel");
        model.addAttribute("preOfferResult",policyCalculatorResult);
        model.addAttribute("totalPremium",totalPremium);
        model.addAttribute("carmodel", carModel);
    }
}
