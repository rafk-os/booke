package com.example.backend.services;

import com.example.backend.exceptions.ConflictException;
import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.model.Book;
import com.example.backend.model.Cart;
import com.example.backend.repository.BookRepository;
import com.example.backend.repository.CartRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

@Service("bookService")
public class BookServiceImpl implements BookService{

    private final BookRepository bookRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public BookServiceImpl(BookRepository bookRepository, CartRepository cartRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }


    @Override
    public void delete(Integer id) {
        Book book;
        try {
            book = bookRepository.findById(id).orElseThrow();
        }
        catch (NoSuchElementException e){
            throw new ResourceNotFoundException( "No book of id " + id + " found.");
        }
        for (Cart cart: book.getCarts())
            cart.getBooks().remove(book);
        book.getCarts().clear();
        bookRepository.save(book);
        bookRepository.delete(book);

    }

    @Override
    public Book add(Book book) {
        try {
            String username =  SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName();
            book.setUser(userRepository.findByUsername(username).orElseThrow());
            book.setCarts(new HashSet<>());
        }
        catch (NoSuchElementException e){
            throw new ResourceNotFoundException( "Please login again. ");
        }
        return bookRepository.save(book);
    }

    @Override
    public Book find(Integer id) {
        try {
            return bookRepository.findById(id).orElseThrow();
        }
        catch (NoSuchElementException e){
            throw new ResourceNotFoundException( "No book of id " + id + " found.");
        }
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Cart addToCart( Integer bookId) {
        Integer cartId;
        try {
             cartId = userRepository.findByUsername( SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName()).orElseThrow().getId();
        }
        catch (NoSuchElementException e){
            throw new ResourceNotFoundException( "Please login again. ");
        }
        Book book;
        Cart cart;
        try {
            book = bookRepository.findById(bookId).orElseThrow();
        }
        catch (NoSuchElementException e){
            throw new ResourceNotFoundException( "No book of id " + bookId + " found.");
        }
        try {
            cart = cartRepository.findById(cartId).orElseThrow();
        }
        catch (NoSuchElementException e){
            throw new ResourceNotFoundException( "No cart of id " + cartId + " found.");
        }
        cart.getBooks().add(book);
        return cartRepository.save(cart);
    }

    @Override
    public void removeFromCart(Integer bookId) {
        Integer cartId;
        try {
            cartId = userRepository.findByUsername( SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName()).orElseThrow().getId();
        }
        catch (NoSuchElementException e){
            throw new ResourceNotFoundException( "Please login again. ");
        }
        Book book;
        Cart cart;
        try {
            book = bookRepository.findById(bookId).orElseThrow();
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("No book of id " + bookId + " found.");
        }
        try {
            cart = cartRepository.findById(cartId).orElseThrow();
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("No cart of id " + cartId + " found.");
        }
        if (!cart.getBooks().contains(book))
            throw new ConflictException("Book does not belong to this cart");

        book.getCarts().remove(cart);
        cart.getBooks().remove(book);
        cartRepository.save(cart);
    }
}
