package com.onpier.library.user;

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
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final UserService userService;

    @GetMapping("/borrowers")
    public ResponseEntity<List<UserEntity>> getAllBorrowers() {
        List<UserEntity> borrowers = userService.getAllBorrowers();
        return ResponseEntity.ok(borrowers);
    }

    @GetMapping("/no-borrowed-books")
    public ResponseEntity<List<UserEntity>> getActiveUsersWithNoBooks() {
        List<UserEntity> users = userService.getActiveUsersWithNoBooks();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/borrowed-on")
    public ResponseEntity<List<UserEntity>> getUsersBorrowedOn(@RequestParam(name = "date") @DateTimeFormat(pattern = "MM/dd/yyyy") LocalDate date) {
        List<UserEntity> users = userService.getUsersBorrowedOn(date);
        return ResponseEntity.ok(users);
    }
}
