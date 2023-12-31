package com.emre.employeeAPI.model;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Employee {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;


    private String fullName;


    @Column(unique=true)
    private String email;

    @DateTimeFormat(pattern="YYYY-MM-DD")
    private Date birthDay;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    private List<Hobby> hobbies;

}
