package com.project.yapayspringboot.model;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Company {
    @NotNull
    @Size(min=9, max=9)
    private String phone;
    @NotNull
    private String name;

    public Company(String companyName, String phoneNumber) {
        this.name = companyName;
        this.phone = phoneNumber;
    }

    public String getName() { return this.name; }
    public String getPhone() { return this.phone;  }

}
