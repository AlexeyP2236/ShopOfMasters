package com.example.shopofmasters.models;

import com.example.shopofmasters.config.ValidatedAuthorization;
import com.example.shopofmasters.config.ValidatedDataPerson;
import com.example.shopofmasters.enumm.Confirmed;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Objects;

//copy
@Entity
@Table(name = "Person")
public class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Логин не может быть пустым", groups = {ValidatedAuthorization.class, ValidatedDataPerson.class}) //групповая валидация
    // Источник https://www.baeldung.com/spring-valid-vs-validated
    @Size(min = 5, max = 100, message = "Логин должен быть от 5 до 100 символов", groups = ValidatedAuthorization.class)
    @Column(name = "login")
    private String login;

    @NotEmpty(message = "Пароль не может быть пустым", groups = ValidatedAuthorization.class)
    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    //DataPerson
    @NotEmpty(message = "Строка не может быть пустой", groups = ValidatedDataPerson.class)
    private String firstName;
    @NotEmpty(message = "Строка не может быть пустой", groups = ValidatedDataPerson.class)
    private String lastName;
    @NotEmpty(message = "Строка не может быть пустой", groups = ValidatedDataPerson.class)
    private String patronymic;
    @NotEmpty(message = "Строка не может быть пустой", groups = ValidatedDataPerson.class)
    private String age;
    @NotEmpty(message = "Строка не может быть пустой", groups = ValidatedDataPerson.class)
    private String telephone;
    @NotEmpty(message = "Строка не может быть пустой", groups = ValidatedDataPerson.class)
    private String dateBirth;
    private String biography;
    private Confirmed confirmed;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(String dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public Confirmed getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Confirmed confirmed) {
        this.confirmed = confirmed;
    }

    @ManyToMany()
    @JoinTable(name = "product_cart", joinColumns = @JoinColumn(name = "person_id"),inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> productList;

    @OneToMany(mappedBy = "person", fetch = FetchType.EAGER)
    private List<Order> orderList;


//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "data_person_id", referencedColumnName = "id", unique = true)
//    private DataPerson dataPerson;
//
//    public DataPerson getDataPerson() {
//        return dataPerson;
//    }
//
//    public void setDataPerson(DataPerson dataPerson) {
//        this.dataPerson = dataPerson;
//    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id && Objects.equals(login, person.login) && Objects.equals(password, person.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password);
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}