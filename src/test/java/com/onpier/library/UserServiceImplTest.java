package com.onpier.library;

import com.onpier.library.user.UserEntity;
import com.onpier.library.user.UserRepository;
import com.onpier.library.user.UserServiceImpl;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private Session session;

    @Mock
    private Query<UserEntity> query;

    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity user;

    @BeforeEach
    public void setUp() {
        user = new UserEntity();
        user.setId(1);
        user.setFirstName("John");
        user.setName("David");
        user.setGender('m');
        user.setMemberSince(LocalDate.now());

        when(entityManager.unwrap(Session.class)).thenReturn(session);
    }

    @AfterEach
    public void teardown() {
        user = null;
    }

    @Test
    public void testGetAllBorrowers() {
        List<UserEntity> expectedUsers = Collections.singletonList(user);

        String hql = "FROM UserEntity u where CONCAT(u.name, ',', u.firstName) in (select b.borrower from BorrowerEntity b)";
        when(session.createQuery(hql)).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedUsers);

        List<UserEntity> result = userService.getAllBorrowers();

        assertEquals(expectedUsers, result);
        verify(session).createQuery(hql);
        verify(query).getResultList();
    }

    @Test
    public void testGetActiveUsersWithNoBooks() {
        List<UserEntity> expectedUsers = Collections.singletonList(user);

        String hql = "FROM UserEntity u where u.memberTill is null and CONCAT(u.name, ',', u.firstName) not in (select b.borrower from BorrowerEntity b)";
        when(session.createQuery(hql, UserEntity.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedUsers);

        List<UserEntity> result = userService.getActiveUsersWithNoBooks();

        assertEquals(expectedUsers, result);
        verify(session).createQuery(hql, UserEntity.class);
        verify(query).getResultList();
    }

    @Test
    public void testGetUsersBorrowedOn() {
        LocalDate date = LocalDate.now();
        List<UserEntity> expectedUsers = Collections.singletonList(user);

        String hql = "FROM UserEntity u where CONCAT(u.name, ',', u.firstName) in " + "(select b.borrower from BorrowerEntity b where b.borrowedFrom = :date)";
        when(session.createQuery(hql, UserEntity.class)).thenReturn(query);
        when(query.setParameter("date", date)).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedUsers);

        List<UserEntity> result = userService.getUsersBorrowedOn(date);

        assertEquals(expectedUsers, result);
        verify(session).createQuery(hql, UserEntity.class);
        verify(query).setParameter("date", date);
        verify(query).getResultList();
    }
}
