package com.onpier.library;

import com.onpier.library.book.BookEntity;
import com.onpier.library.book.BookRepository;
import com.onpier.library.book.BookServiceImpl;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.query.Query;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private Session session;

    @Mock
    private Query<BookEntity> query;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    public void setUp() {
        when(entityManager.unwrap(Session.class)).thenReturn(session);
    }

    @Test
    public void testGetBooksByUser() {
        List<BookEntity> expectedBooks = new ArrayList<>();
        BookEntity firstBook = new BookEntity();
        firstBook.setTitle("First Book");
        expectedBooks.add(firstBook);

        BookEntity secondBook = new BookEntity();
        secondBook.setTitle("Second Book");
        expectedBooks.add(secondBook);

        String hql = "FROM BookEntity b where b.title in (select br.book from BorrowerEntity br where br.borrower = :borrower and br.borrowedFrom = :date)";
        when(session.createQuery(hql, BookEntity.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedBooks);

        String borrower = "John";
        LocalDate date = LocalDate.of(2023, 1, 1);

        List<BookEntity> actualBooks = bookService.getBooksByUser(borrower, date);

        assertNotNull(actualBooks);
        assertEquals(expectedBooks, actualBooks);
        verify(query).setParameter("borrower", borrower);
        verify(query).setParameter("date", date);
        verify(query).getResultList();
        verify(session).createQuery(hql, BookEntity.class);
    }

    @Test
    public void getNonBorrowedBooks() {
        String hql = "FROM BookEntity b where b.title not in (select br.book from BorrowerEntity br)";
        when(session.createQuery(hql, BookEntity.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<>());

        List<BookEntity> actualBooks = bookService.getNonBorrowed();

        assertNotNull(actualBooks);
        verify(query).getResultList();
        verify(session).createQuery(hql, BookEntity.class);
    }
}
