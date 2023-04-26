package com.example.shopofmasters.services;

import com.example.shopofmasters.models.Person;
import com.example.shopofmasters.repositories.PersonRepository;
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

    //Смена роли у пользователя
    @Transactional
    public void rolePerson(Person person) {
        personRepository.findById(person.getId()).ifPresent(i -> {
            i.setRole(person.getRole());
            personRepository.save(i);
        });
    }
    @Transactional
    public void addInfoPerson(Person person) {
        personRepository.findById(person.getId()).ifPresent(i -> {
            i.setLogin(person.getLogin());
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
}
