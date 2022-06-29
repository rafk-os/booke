package com.example.backend.controllers;

import com.example.backend.model.Book;
import com.example.backend.model.Cart;
import com.example.backend.services.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/")
public class BookController {
    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }



    @Operation(
            operationId = "createBook",
            responses = {
                    @ApiResponse(responseCode = "201", description = "A book was created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class))),
                    @ApiResponse(responseCode = "401", description = "User not authenticated."),
                    @ApiResponse(responseCode = "403", description = "User not authorized to create book."),
                    @ApiResponse(responseCode = "404", description = "User not found.")
            }
    )
    @PostMapping(value = "/books", produces = { "application/json" }, consumes = { "application/json" })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        Book result = bookService.add(book);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Operation(
            operationId = "getBook",
            responses = {
                    @ApiResponse(responseCode = "200", description = "A book of given id", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Book.class))),
                    @ApiResponse(responseCode = "401", description = "User not authenticated."),
                    @ApiResponse(responseCode = "404", description = "Book not found.")
            }
    )
    @GetMapping(value = "/books/{id}", produces = { "application/json" })
    public ResponseEntity<Book> getBook(@PathVariable("id") Integer id) {
        Book result = bookService.find(id);

        return ResponseEntity.ok(result);
    }

    @Operation(
            operationId = "getBooks",
            responses = {
                    @ApiResponse(responseCode = "200", description = "All books in database", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "401", description = "User not authenticated."),
            }
    )
    @GetMapping(value = "/books", produces = { "application/json" })
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> result = bookService.findAll();

        return ResponseEntity.ok(result);
    }

    @Operation(
            operationId = "deleteBook",
            responses = {
                    @ApiResponse(responseCode = "204", description = "A book was deleted"),
                    @ApiResponse(responseCode = "401", description = "User not authenticated."),
                    @ApiResponse(responseCode = "403", description = "User not authorized to delete book."),
                    @ApiResponse(responseCode = "404", description = "Book not found.")
            }
    )
    @DeleteMapping(value = "/books/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") Integer id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            operationId = "addBookToCart",
            responses = {
                    @ApiResponse(responseCode = "200", description = "A book was added", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cart.class))),
                    @ApiResponse(responseCode = "401", description = "User not authenticated."),
                    @ApiResponse(responseCode = "404", description = "Book or cart not found.")
            }
    )
    @PostMapping( value = "/books/{id}/cart", produces = { "application/json" } , consumes = { "application/json" })
    public ResponseEntity<Cart> addBookToCart(@PathVariable("id") Integer id) {
        Cart result = bookService.addToCart(id);

        return ResponseEntity.ok(result);
    }

    @Operation(
            operationId = "removeBookFromCart",
            responses = {
                    @ApiResponse(responseCode = "200", description = "A book was deleted from cart"),
                    @ApiResponse(responseCode = "401", description = "User not authenticated."),
                    @ApiResponse(responseCode = "404", description = "Cart or book not found.")
            }
    )
    @DeleteMapping(value = "/books/{id}/cart", produces = { "application/json" })
    public ResponseEntity<Void> removeBookFromCart(@PathVariable("id") Integer id) {
        bookService.removeFromCart(id);
        return ResponseEntity.ok().build();
    }

}
