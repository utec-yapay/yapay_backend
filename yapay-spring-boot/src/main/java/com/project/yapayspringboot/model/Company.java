package com.project.yapayspringboot.model;

import java.util.UUID;

public class Company {
    private String phone;
    private String name;

    public Company(String companyName, String phoneNumber) {
        this.name = companyName;
        this.phone = phoneNumber;
    }

    public String getName() { return name; }
    public String getPhone() { return phone;  }

}
