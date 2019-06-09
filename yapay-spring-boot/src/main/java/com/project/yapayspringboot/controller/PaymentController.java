package com.project.yapayspringboot.controller;

import com.project.yapayspringboot.model.Payment;

import org.springframework.web.bind.annotation.*;

import java.util.Hashtable;
import java.util.UUID;

@RestController
public class PaymentController {
    // TODO: Error handling

    @PostMapping("/payments")
    public String createPayment(@RequestBody Payment newpayment){
        // TODO: Valid request body

        /* Creates payment and returns the JSON
        *  Web Token (JWT) that will be use to
        *  create a QR.
        *
        *  JWT payload includes: paymentId, companyPhone,
        *                        companyName, amount
        *
        *  Throws exception if request body is missing any
        *  of the parameters: companyName, companyPhone, amount
        */

        return newpayment.generateJwt();
    }

    @GetMapping("/payments/confirm")
    public synchronized void confirmPayment(@RequestHeader("paymentId") UUID paymentId) throws IllegalArgumentException{
        /* Confirms payment with id paymentId and
        * inserts payment to database
        *
        * Throws exception if payment ID wasn't
        * specified, doesn't exist or is already
        * confirmed  */

        if (paymentId==null){
            throw new IllegalArgumentException("Missing Payment ID");
        }

        Payment paymentToConfirm;
        // TODO: Find payment in database
        //       If payment doesn't exist: throw error

        if (!paymentToConfirm.confirm()){
            throw new IllegalArgumentException("Payment with ID " + paymentId + " doesn't exist");
        }

    }

    @GetMapping("/payments/isconfirmed")
    public Boolean isconfirmPayment(@RequestHeader("paymentId") UUID paymentId) throws IllegalArgumentException{
        /* Throws exception if payment ID wasn't
        * specified or doesn't exist */

        if (paymentId==null) {
            throw new IllegalArgumentException("Missing Payment ID");
        }

        Payment paymentToVerify;
        // TODO: Find payment in database
        //       If payment doesn't exist: throw error

        return paymentToVerify.isConfirmed();
    }

}
