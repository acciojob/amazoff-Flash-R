package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    public void addOrder(Order order) {
        orderRepository.addOrder(order);
    }
}
