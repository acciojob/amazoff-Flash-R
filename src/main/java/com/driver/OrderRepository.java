package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
        unassignedOrders.add(order.getId());
    }

    public void addPartner(String partnerId) {
        DeliveryPartner partner = new DeliveryPartner(partnerId);
        partnerDb.put(partnerId, partner);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
            //get the list of orders associated with a partner if it doesn't exist create an empty arraylist
            List<String> list = pairDB.getOrDefault(partnerId, new ArrayList<>());
            list.add(orderId);
            pairDB.put(partnerId, list);
            //set the number of orders in partner
            int newNumber = partnerDb.get(partnerId).getNumberOfOrders() + 1;
            partnerDb.get(partnerId).setNumberOfOrders(newNumber);
            // remove from unassigned orders
            unassignedOrders.remove(orderId);

    }
}
