package com.example.backend.services;

import com.example.backend.model.Book;
import com.example.backend.model.Cart;

import java.util.List;
import java.util.Set;

public interface BookService {
    void delete(Integer id);
    Book add(Book book);
    Book find(Integer id);
    List<Book> findAll();
    Cart addToCart(Integer bookId);
    void removeFromCart( Integer bookId);
}
