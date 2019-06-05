package com.project.yapayspringboot.controller;

import com.project.yapayspringboot.model.Payment;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    @PostMapping("/payments")
    public Payment createPayment(@RequestBody Payment payment){
        return payment;
    }




}
