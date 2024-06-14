package com.onpier.library.user;

import java.time.LocalDate;
import java.util.List;

public interface UserService {

    List<UserEntity> getAllBorrowers();

    List<UserEntity> getActiveUsersWithNoBooks();

    List<UserEntity> getUsersBorrowedOn(LocalDate date);
}
