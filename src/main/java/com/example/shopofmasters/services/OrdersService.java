package com.example.shopofmasters.services;

import com.example.shopofmasters.enumm.Status;
import com.example.shopofmasters.models.Cart;
import com.example.shopofmasters.models.Order;
import com.example.shopofmasters.models.Product;
import com.example.shopofmasters.repositories.CartRepository;
import com.example.shopofmasters.repositories.OrderRepository;
import com.example.shopofmasters.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class OrdersService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductService productService;

    private final PersonDetailsService personDetailsService;
    @Autowired
    public OrdersService(OrderRepository orderRepository, CartRepository cartRepository, ProductService productService, PersonDetailsService personDetailsService) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.personDetailsService = personDetailsService;
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
    public List<Order> getOrderId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        List<Order> optionalOrder = orderRepository.findByPerson(personDetails.getPerson());
        return optionalOrder;
    }
    //Cart
    @Transactional
    public void addCart (Cart cart){
        cartRepository.save(cart);
    }
    public List<Product> getOrderForPersonId(int id) {
        List<Cart> cartList = cartRepository.findByPersonId(id);
        List<Product> productList = new ArrayList<>();
        // Получаем продукты из корзины по id товара
        for (Cart cart: cartList) {
            productList.add(productService.getProductId(cart.getProductId()));
        }
        return productList;
    }

    public void deleteOrderForPersonId(int id){
        cartRepository.deleteCartByProductId(id);
    }

    @Transactional
    public void saveAndDeleteCart(int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        String uuid = UUID.randomUUID().toString();
        for (Product product : getOrderForPersonId(id)) {
            Order newOrder = new Order(uuid, product, personDetails.getPerson(), 1, product.getPrice(), Status.Оформлен);
            orderRepository.save(newOrder);
            //удаление с корзины
            cartRepository.deleteCartByProductId(product.getId());
        }
    }
}
