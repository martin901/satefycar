package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("")
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
    @GetMapping("/error")
    public String errorPage(HttpServletRequest httpRequest){
        int httpErrorCode = getErrorCode(httpRequest);

        switch (httpErrorCode){
            case 400: {
                return "400-page";
            }
            case 401: {
                return "401-page";
            }
            case 403: {
                return "403-page";
            }
            case 404: {
                return "404-page";
            }
            case 500: {
                return "500-page";
            }
            default: {
                return "error";
            }
        }
    }

    private int getErrorCode(HttpServletRequest httpRequest){
        return (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
