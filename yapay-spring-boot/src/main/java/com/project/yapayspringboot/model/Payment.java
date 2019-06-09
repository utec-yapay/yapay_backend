package com.project.yapayspringboot.model;

import java.util.UUID;

public class Payment {
    private Long id;
    private Company company;
    private float totalAmount;
    private boolean confirmed = false;

    public Payment(String companyName, String companyPhone, float amount) {
        company = new Company(companyName, companyPhone);
        totalAmount = amount;
        // TODO: Add to database
        // id = id in data base
    }

    public String generateJwt(){
        // TODO: Generate JWT

        return "";
    }

    public boolean confirm(){
        if (confirmed) return false;
        confirmed = true;

        // TODO: Find payment in database and update confirmed attribute


        return true;
    }

    public Long getId(){ return id; }
    public boolean isConfirmed() { return confirmed; }
}
