package com.example.shopofmasters.services;

import com.example.shopofmasters.enumm.Confirmed;
import com.example.shopofmasters.models.Person;
import com.example.shopofmasters.repositories.PersonRepository;
import com.example.shopofmasters.security.PersonDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;


    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //Проверка пользователя по логину
    public Person findByLogin(Person person){
        Optional<Person> person_db = personRepository.findByLogin(person.getLogin());
        return person_db.orElse(null);
    }

    //Регистрация пользователя
    @Transactional
    public void register(Person person) {
        //хеширование паролей при регистрации
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER"); //роль по умолчанию
        person.setConfirmed(Confirmed.Пользователь);
        personRepository.save(person);
    }

    //Получить всех пользователей
    public List<Person> getAllPerson(){
        return personRepository.findAll();
    }

    //Метод по получению пользователя по id
    public Person getPersonId(int id){
        Optional<Person> optionalPerson = personRepository.findById(id);
        return optionalPerson.orElse(null);
    }

    //Заполнение/смена данных пользователя, в том числе роли
    @Transactional
    public void rolePerson(Person person) {
        personRepository.findById(person.getId()).ifPresent(i -> {
            i.setRole(person.getRole());
            personRepository.save(i);
        });
    }
    //Заполнение/смена данных пользователя по id
    @Transactional
    public void addInfoPerson(int id, Person person) {
        person.setId(id);
        personRepository.findById(person.getId()).ifPresent(i -> {
            i.setFirstName(person.getFirstName());
            i.setLastName(person.getLastName());
            i.setPatronymic(person.getPatronymic());
            i.setAge(person.getAge());
            i.setTelephone(person.getTelephone());
            i.setDateBirth(person.getDateBirth());
            i.setBiography(person.getBiography());
            personRepository.save(i);
        });
    }
    @Transactional
    public void confirmedPerson(Person person) {
        personRepository.findById(person.getId()).ifPresent(i -> {
            i.setConfirmed(person.getConfirmed());
        });
    }


    @Transactional
    public int personIdSecurityContextHolder(){
        // Извлекаем объект аутентифицированного пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        // Извлекаем id пользователя из объекта
        int id = personDetails.getPerson().getId();
        return id;
    }

}
