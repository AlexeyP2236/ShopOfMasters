package com.example.shopofmasters.services;

import com.example.shopofmasters.models.Person;
import com.example.shopofmasters.repositories.PersonRepository;
import com.example.shopofmasters.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {
    private final PersonRepository personRepository;

    @Autowired
    public PersonDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    //Поиск пользователя по имени пользователя
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Получаем пользователя из таблицы по логину с формы аутентификации
        Optional<Person> person = personRepository.findByLogin(username);

        // Если пользователь не был найден
        if(person.isEmpty()){
            // Выбрасываем исключение что данный пользователь не найден
            // Данное исключение будет поймано Spring Security и сообщение будет выведено на страницу
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        return new PersonDetails(person.get());
    }

}
