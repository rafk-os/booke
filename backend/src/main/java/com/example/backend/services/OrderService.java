package com.example.backend.services;

import com.example.backend.model.Cart;
import com.example.backend.model.Orders;
import com.example.backend.model.Status;

public interface OrderService {
    Orders place(Orders order);

    Orders changeStatus(Integer id, Status status);

    Orders find(Integer id);

    Status checkAndChangeIntoStatus(String status);
}
