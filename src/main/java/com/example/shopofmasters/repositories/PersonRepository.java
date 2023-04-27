package com.example.shopofmasters.repositories;

import com.example.shopofmasters.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {


    //Метод для поиска сущности Person по логину
    Optional<Person> findByLogin(String login);

    @Modifying
    @Query(value = "update person set first_name = ?1, last_name = ?2, patronymic = ?3, age = ?4, telephone = ?5, date_birth = ?6, biography = ?7 where id = ?8", nativeQuery = true)
    List<Person> setFixedPerson (String firstName, String lastName, String patronymic, String age, String telephone, String dateBirth, String biography, int id);
}
