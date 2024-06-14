package com.onpier.library.book;

import java.time.LocalDate;
import java.util.List;

public interface BookService {

    List<BookEntity> getBooksByUser(String borrower, LocalDate date);

    List<BookEntity> getNonBorrowed();
}
