package com.example.shopofmasters.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {

    //Метод аутентификации
    @GetMapping("/authentication")
    public String login(){
        return "authentication";
    }



}

