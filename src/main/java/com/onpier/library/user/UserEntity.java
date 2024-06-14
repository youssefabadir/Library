package com.onpier.library.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String firstName;

    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate memberSince;

    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate memberTill;
    private char gender;
}
