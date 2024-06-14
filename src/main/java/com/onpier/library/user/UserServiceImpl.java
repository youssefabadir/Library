package com.onpier.library.user;

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
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @Override
    public List<UserEntity> getAllBorrowers() {
        Session session = entityManager.unwrap(Session.class);
        String hql = "FROM UserEntity u where CONCAT(u.name, ',', u.firstName) in (select b.borrower from BorrowerEntity b)";
        Query query = session.createQuery(hql);
        return (List<UserEntity>) query.getResultList();
    }

    @Override
    public List<UserEntity> getActiveUsersWithNoBooks() {
        Session session = entityManager.unwrap(Session.class);
        String hql = "FROM UserEntity u where u.memberTill is null and CONCAT(u.name, ',', u.firstName) not in (select b.borrower from BorrowerEntity b)";
        Query query = session.createQuery(hql, UserEntity.class);
        return (List<UserEntity>) query.getResultList();
    }

    @Override
    public List<UserEntity> getUsersBorrowedOn(LocalDate date) {
        Session session = entityManager.unwrap(Session.class);
        String hql =
            "FROM UserEntity u where CONCAT(u.name, ',', u.firstName) in " + "(select b.borrower from BorrowerEntity b where b.borrowedFrom = :date)";
        Query query = session.createQuery(hql, UserEntity.class);
        query.setParameter("date", date);
        return (List<UserEntity>) query.getResultList();
    }

    @PostConstruct
    private void loadUsers() throws IOException, CsvException, ParseException {
        try (CSVReader reader = new CSVReader(new FileReader("src/main/resources/data/user.csv"))) {
            reader.readNext();
            String[] record;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            while ((record = reader.readNext()) != null) {
                if (record[0].isBlank()) { //To avoid loading empty string
                    break;
                }
                UserEntity user = new UserEntity();
                user.setName(record[0]);
                user.setFirstName(record[1]);
                user.setMemberSince(LocalDate.parse(record[2], formatter));
                if (record[3].isBlank()) {
                    user.setMemberTill(null);
                } else {
                    user.setMemberTill(LocalDate.parse(record[3], formatter));
                }
                user.setGender(record[4].charAt(0));
                userRepository.save(user);
            }
        }
    }
}
