package com.example.shopofmasters.repositories;

import com.example.shopofmasters.models.Order;
import com.example.shopofmasters.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByPerson(Person person);

    // Данный метод позволяет получить заказы по номеру заказа
    //@Query(value = "select * from orders where (lower(number) SIMILAR TO '%?1{4}')", nativeQuery = true)
    List<Order> findOrderByNumberContainingIgnoreCase (String number);

    @Modifying
    @Query(value = "update orders set status = ?1", nativeQuery = true)
    List<Order> setFixedStatus (Integer status);
    //List<Order> setFixedStatusForOrder (int status, int id);
}

