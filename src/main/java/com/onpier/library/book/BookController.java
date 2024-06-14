package com.onpier.library.book;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookController {

    private final BookService bookService;

    @GetMapping("/borrowed-by")
    public ResponseEntity<List<BookEntity>> getBooksByUser(@RequestParam(name = "borrower") String borrower,
                                                           @RequestParam(name = "date") @DateTimeFormat(pattern = "MM/dd/yyyy") LocalDate date) {
        List<BookEntity> books = bookService.getBooksByUser(borrower, date);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/non-borrowed")
    public ResponseEntity<List<BookEntity>> getNonBorrowed() {
        List<BookEntity> books = bookService.getNonBorrowed();
        return ResponseEntity.ok(books);
    }
}
