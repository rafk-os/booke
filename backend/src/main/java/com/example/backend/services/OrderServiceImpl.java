package com.example.backend.services;

import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.model.Orders;
import com.example.backend.model.Status;
import com.example.backend.repository.OrderRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.NoSuchElementException;

@Service("orderService")
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Orders place(Orders order) {
        try {
            String username =  SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName();
            order.setUser(userRepository.findByUsername(username).orElseThrow());
            order.setStatus(Status.OPEN);
            order.setBooks(new HashSet<>());
            order.getBooks().addAll(order.getUser().getCart().getBooks());
        }
        catch (NoSuchElementException e){
            throw new ResourceNotFoundException( "Please login again. ");
        }
        return orderRepository.save(order);
    }

    @Override
    public Orders changeStatus(Integer id, Status status) {
        Orders order;
        try {
            order =  orderRepository.findById(id).orElseThrow();
        }
        catch (NoSuchElementException e){
            throw new ResourceNotFoundException( "No order of id " + id + " found.");
        }
        order.setStatus(status);
        orderRepository.save(order);
        return order;
    }

    @Override
    public Orders find(Integer id) {
        try {
            return orderRepository.findById(id).orElseThrow();
        }
        catch (NoSuchElementException e) {
            throw new ResourceNotFoundException( "No order of id " + id + " found.");
        }
    }

    @Override
    public Status checkAndChangeIntoStatus(String status) {
        Status newStatus;
        try {
            newStatus = Status.valueOf(status);
        }
        catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException( "No status of name " + status + " found.");
        }
        return newStatus;
    }
}
