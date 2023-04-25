package com.example.shopofmasters.repositories;

import com.example.shopofmasters.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {


    //Метод для поиска сущности Person по логину
    Optional<Person> findByLogin(String login);
}
