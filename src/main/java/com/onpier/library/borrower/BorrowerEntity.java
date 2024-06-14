package com.onpier.library.borrower;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class BorrowerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String borrower;
    private String book;

    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate borrowedFrom;

    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate borrowedTo;
}
