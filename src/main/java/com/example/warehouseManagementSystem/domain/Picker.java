package com.example.warehouseManagementSystem.domain;


import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "picker")
public class Picker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String middlename;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(unique = true)
    private String badgeBarcode; 

    @Enumerated(EnumType.STRING)
    private PickerStatus status; 

    public Picker() {

    }

    public Picker(Long id, String firstName, String badgeBarcode, PickerStatus status) {
        this.id = id;
        this.firstName = firstName;
        this.badgeBarcode = badgeBarcode;
        this.status = status;
    }


    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getBadgeBarcode() {
        return badgeBarcode;
    }

    public void setBadgeBarcode(String badgeBarcode) {
        this.badgeBarcode = badgeBarcode;
    }

    public PickerStatus getStatus() {
        return status;
    }

    public void setStatus(PickerStatus status) {
        this.status = status;
    }
}
