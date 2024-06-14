package com.onpier.library.book;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final EntityManager entityManager;

    @PostConstruct
    private void loadUsers() throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(new FileReader("src/main/resources/data/books.csv"))) {
            reader.readNext();
            String[] record;
            while ((record = reader.readNext()) != null) {
                if (record[0].isBlank()) {//To avoid loading empty string
                    break;
                }
                BookEntity book = new BookEntity();
                book.setTitle(record[0]);
                book.setAuthor(record[1]);
                book.setGenre(record[2]);
                book.setPublisher(record[3]);
                bookRepository.save(book);
            }
        }
    }

    @Override
    public List<BookEntity> getBooksByUser(String borrower, LocalDate date) {
        Session session = entityManager.unwrap(Session.class);
        String hql = "FROM BookEntity b where b.title in (select br.book from BorrowerEntity br where br.borrower = :borrower and br.borrowedFrom = :date)";
        Query query = session.createQuery(hql, BookEntity.class);
        query.setParameter("borrower", borrower);
        query.setParameter("date", date);
        return (List<BookEntity>) query.getResultList();
    }

    @Override
    public List<BookEntity> getNonBorrowed() {
        Session session = entityManager.unwrap(Session.class);
        String hql = "FROM BookEntity b where b.title not in (select br.book from BorrowerEntity br)";
        Query query = session.createQuery(hql, BookEntity.class);
        return (List<BookEntity>) query.getResultList();
    }
}
