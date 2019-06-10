package com.project.yapayspringboot.model;

public class Company {
    private String phone;
    private String name;

    public Company(String companyName, String phoneNumber) {
        this.name = companyName;
        this.phone = phoneNumber;
    }

    public String getName() { return this.name; }
    public String getPhone() { return this.phone;  }

}
