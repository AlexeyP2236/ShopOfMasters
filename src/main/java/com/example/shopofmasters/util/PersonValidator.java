package com.example.shopofmasters.util;

import com.example.shopofmasters.models.Person;
import com.example.shopofmasters.services.PersonService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {


    private final PersonService personService;

    public PersonValidator(PersonService personService) {
        this.personService = personService;
    }

    // В данном методу указываем для какой модели предназначен данный валидатор, т.е. пользователя
    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    //Проверка на ошибки, в данном случае проверка на повтор пользователей
    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        if(personService.findByLogin(person) != null){
            errors.rejectValue("login", "", "Данный логин уже зарегистрировался в системе");
        }
    }
}
