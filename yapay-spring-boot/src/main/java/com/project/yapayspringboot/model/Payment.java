package com.project.yapayspringboot.model;

import java.util.UUID;

public class Payment {
    private UUID id = UUID.randomUUID();
    private Company company;
    private QR qr;
    private float totalAmount;

    public Payment(String companyName, String companyPhone, float amount) {
        company = new Company(companyName, companyPhone);
        totalAmount = amount;
        qr = new QR(id, company, totalAmount);
    }

    public String getQrData(){ return qr.getQrData(); }
    public UUID getId(){ return id; }
}
