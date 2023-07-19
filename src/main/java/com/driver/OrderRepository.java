package com.driver;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Repository
public class OrderRepository {
//    hashmaps for database
    HashMap<String, Order> orderDb = new HashMap<>();
    HashMap<String, DeliveryPartner> partnerDb = new HashMap<>();
    HashMap<String, List<String>> pairDB = new HashMap<>();
    HashSet<String> unassignedOrders = new HashSet<>();
    public void addOrder(Order order) {
        orderDb.put(order.getId(), order);
    }
}
