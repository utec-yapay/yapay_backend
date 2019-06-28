package com.project.yapayspringboot.model;

import java.time.LocalDateTime;
import java.util.List;

public class DataReport {

    private List<Payment> payments;
    private LocalDateTime sent_at = LocalDateTime.now();

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }
}
