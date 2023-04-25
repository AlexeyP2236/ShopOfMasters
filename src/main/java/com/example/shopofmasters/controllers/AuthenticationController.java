package com.example.shopofmasters.controllers;

import com.example.shopofmasters.models.Person;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class AuthenticationController {

    //Метод аутентификации
    @GetMapping("/authentication")
    public String login(){
        return "authentication";
    }



}

