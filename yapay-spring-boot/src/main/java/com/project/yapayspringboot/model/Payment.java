package com.project.yapayspringboot.model;

public class Payment {
    private Long id;
    private Company company;
    private Float totalAmount;
    private Boolean confirmed = false;

    public Payment(String companyName, String companyPhone, Float amount) {
        /* This constructor is used when creating a
        *  new payment.
        *  The controller inserts the object to the
        *  database, which returns an id. Then, it
        * sets the payment id */

        company = new Company(companyName, companyPhone);
        totalAmount = amount;
        confirmed = false;
    }

    public Payment(String companyName, String companyPhone, Float amount, Boolean isconfirmed, Long paymentId) {
        /* This constructor is used when creating a
        * payment from the database */

        company = new Company(companyName, companyPhone);
        totalAmount = amount;
        id = paymentId;
        confirmed = isconfirmed;
    }

    public String generateJwt(){
        // TODO: Generate JWT

        return "";
    }



    public Boolean confirm(){
        if (confirmed) return false;
        return confirmed = true;
    }

    public void setId(Long id) { this.id = id; }

    public Float getTotalAmount() { return totalAmount; }
    public Company getCompany() { return company; }
    public Long getId(){ return id; }
    public Boolean isConfirmed() { return confirmed; }


}