package com.example.shopofmasters.services;

import com.example.shopofmasters.models.Order;
import com.example.shopofmasters.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OrdersService {
    private OrderRepository orderRepository;
    @Autowired
    public OrdersService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    //Получить список заказов
    public List<Order> getOrdersList(){
        return orderRepository.findAll();
    }

    //Поиск заказа
    public List<Order> searchOrders(String number){
        return orderRepository.findOrderByNumberContainingIgnoreCase(number);
    }

    //Показать заказ
    public Order getOrdersId(int id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        return optionalOrder.orElse(null);
    }

    //Изменение статуса
    //Источник https://stackoverflow.com/a/72706965
    //Рабочий метод, если надо 1 или несколько строк в листе заменить
    @Transactional
    public void editOrder(Order order) {
        orderRepository.findById(order.getId()).ifPresent(i -> {
            i.setStatus(order.getStatus());
            orderRepository.save(i);
        });
    }
}
