package com.project.yapayspringboot.controller;

import com.project.yapayspringboot.model.Payment;

import org.springframework.web.bind.annotation.*;

import java.util.Hashtable;
import java.util.UUID;

@RestController
public class PaymentController {
    // TODO: Error handling

    Hashtable<UUID, Payment> unconfirmedPayments = new Hashtable<UUID, Payment>();

    @PostMapping("/payments")
    public Payment createPayment(@RequestBody Payment newpayment){
        unconfirmedPayments.put(newpayment.getId(), newpayment);
        return newpayment;
    }

    @GetMapping("/payments/confirm")
    public synchronized void confirmPayment(@RequestHeader("paymentId") UUID paymentId) throws IllegalArgumentException{
        /* Confirms payment with id reqPaymentId,
        * inserts payment to database and removes
        * its record from unconfirmedPayments.
        *
        * Throws exception if payment ID wasn't
        * specified, is invalid or is already
        * confirmed  */

        Payment paymentToConfirm;
        if (paymentId==null || (paymentToConfirm = unconfirmedPayments.get(paymentId))==null) {
            throw new IllegalArgumentException("Invalid, missing or already confirmed Payment ID");
        }

        paymentToConfirm.confirm();
        unconfirmedPayments.remove(paymentId);

    }

    @GetMapping("/payments/isconfirmed")
    public Boolean isconfirmPayment(@RequestHeader("paymentId") UUID paymentId) throws IllegalArgumentException{
        // TODO: Is there a way to differentiate an invalid payment from a confirmed one?

        /* Returns false if payment is confirmed
        * or invalid. Otherwise, returns true
        *
        * Throws exception if payment ID wasn't
        * specified */
        if (paymentId==null) throw new IllegalArgumentException("Missing Payment ID");

        return !unconfirmedPayments.containsKey(paymentId);
    }

}
