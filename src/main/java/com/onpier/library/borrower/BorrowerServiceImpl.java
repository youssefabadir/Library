package com.onpier.library.borrower;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BorrowerServiceImpl {

    private final BorrowerRepository borrowerRepository;

    @PostConstruct
    private void loadUsers() throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(new FileReader("src/main/resources/data/borrowed.csv"))) {
            reader.readNext();
            String[] record;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            while ((record = reader.readNext()) != null) {
                if (record[0].isBlank()) { //To avoid loading empty string
                    break;
                }
                BorrowerEntity borrower = new BorrowerEntity();
                borrower.setBorrower(record[0]);
                borrower.setBook(record[1]);
                borrower.setBorrowedFrom(LocalDate.parse(record[2], formatter));
                borrower.setBorrowedTo(LocalDate.parse(record[3], formatter));
                borrowerRepository.save(borrower);
            }
        }
    }
}
