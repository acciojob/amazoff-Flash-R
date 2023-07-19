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

    public Order getOrderById(String orderId) {
        return orderDb.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return partnerDb.get(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        return pairDB.get(partnerId).size();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return pairDB.get(partnerId);
    }

    public List<String> getAllOrders() {
        List<String> allOrders = new ArrayList<>();
        for(String orderId : orderDb.keySet()){
            allOrders.add(orderId);
        }

        return allOrders;
    }

    public Integer getCountOfUnassignedOrders() {
        return unassignedOrders.size();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        //convert the string time to integer
        String times[] = time.split(":");
        int hours = Integer.parseInt(times[0]);
        int mins = Integer.parseInt(times[1]);

        int totalTime = (hours*60) + mins;

        int count = 0; //to count the number of orders left

        List<String> partnerOrders = pairDB.getOrDefault(partnerId, new ArrayList<>());
        if(partnerOrders.size() == 0) return 0;

        for(String orderId : partnerOrders){
            Order currentOrder = orderDb.get(orderId);
            if(currentOrder.getDeliveryTime() > totalTime)
                count++;

        }
        return count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        String time = "00:00";
        int maxTime = 0;

        List<String> partnerOrders = pairDB.getOrDefault(partnerId, new ArrayList<>());
        if(partnerOrders.size() == 0) return time;

        // the loop to get the maximum delivery time
        for(String orderId : partnerOrders){
            Order order = orderDb.get(orderId);
            int deliveryTime = order.getDeliveryTime();
            if(deliveryTime > maxTime)
                maxTime = deliveryTime;
        }
        // convert the time to string
        int hours = maxTime / 60;
        int minutes = maxTime % 60;

        if(hours < 10){
            time = "0" + hours + ":";
        }else {
            time = hours + ":";
        }
        if(minutes < 9)
            time += "0" + minutes;
        else
            time += minutes;

        return time;

    }

    public void deletePartnerById(String partnerId) {
        if(!pairDB.isEmpty()){
            unassignedOrders.addAll(pairDB.get(partnerId));
        }

        partnerDb.remove(partnerId);

        pairDB.remove(partnerId);

    }

    public void deleteOrderById(String orderId) {
        //Delete an order and the corresponding partner should be unassigned
        if (orderDb.containsKey(orderId)) {
            if (unassignedOrders.contains(orderId)) {
                unassignedOrders.remove(orderId);
            } else {

                for (String str : pairDB.keySet()) {
                    List<String> list = pairDB.get(str);
                    list.remove(orderId);
                }
            }
        }
    }
}
