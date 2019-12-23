package com.example.demo.controllers;

import com.example.demo.models.CarBrand;
import com.example.demo.models.CarModel;
import com.example.demo.models.utils.MulticriteriaTableHelper;
import com.example.demo.models.utils.PolicyCalculator;
import com.example.demo.services.CarBrandService;
import com.example.demo.services.CarModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/calculator")
public class CalculatorController {
    private CarBrandService carBrandService;
    private CarModelService carModelService;
    private MulticriteriaTableHelper helper;

    @Autowired
    public CalculatorController(CarBrandService carBrandService, CarModelService carModelService, MulticriteriaTableHelper helper) {
        this.carBrandService = carBrandService;
        this.carModelService = carModelService;
        this.helper = helper;
        helper.getFromDatabase();
    }

    @GetMapping
    public String showCalculator(Model model, HttpSession session) {
        PolicyCalculator policyCalculatorResult = (PolicyCalculator) session.getAttribute("calculatorResult");
        boolean isNotCreated = policyCalculatorResult == null;
        PolicyCalculator policyCalculator;
        if (!isNotCreated) {
            model.addAttribute("calculatorResult", policyCalculatorResult);
            policyCalculator = policyCalculatorResult;
            session.setAttribute("totalPremium", policyCalculator.calculateTotalPremium());
        } else {
            policyCalculator = new PolicyCalculator();
        }
        model.addAttribute("carBrands", carBrandService.getAll());
        model.addAttribute("calculator", policyCalculator);
        model.addAttribute("isNotCreated", isNotCreated);
        return "calculator";
    }

    @GetMapping("/models")
    public @ResponseBody
    List<CarModel> findAllModels(@RequestParam(value = "carBrandId") Long carBrandId) {
        CarBrand carBrand = carBrandService.getOne(carBrandId);
        return carModelService.getAllByCarBrand(carBrand);
    }

    @PostMapping
    public String calculate(@Valid @ModelAttribute("calculator") PolicyCalculator calculator, BindingResult result, HttpSession session, Model model) {
        if(result.hasErrors()){
            model.addAttribute("hasErrors", true);
            model.addAttribute("carBrands", carBrandService.getAll());
            return "calculator";
        }
        try {
            calculator.getOffer().setTotalPremium(calculator.calculateTotalPremium());
        } catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        session.setAttribute("calculatorResult", calculator);
        return "redirect:/calculator";
    }
}
