package com.project.yapayspringboot.controller;

import com.project.yapayspringboot.model.Payment;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@CrossOrigin
public class PaymentController {
    private HashMap<Long, SseEmitter> emitters = new HashMap<>();
    // TODO: Error handling

    @RequestMapping("/confirmEvent/{id}")
    public SseEmitter confirmEvent(@PathVariable(value="id") Long id) {
        SseEmitter sseEmitter = new SseEmitter();
        emitters.put(id, sseEmitter);
        System.out.println("Inserting key: " + id + " - value: "+ sseEmitter);
        //sseEmitter.onCompletion(() -> emitters.remove(id));
        return sseEmitter;
    }

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
    public synchronized boolean confirmPayment(@RequestHeader("paymentId") Long paymentId)
            throws IllegalArgumentException{
        System.out.println("Hit: " + paymentId);
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

        // TMP: uncomment this
//        if (!paymentToConfirm.confirm()){
//            throw new IllegalArgumentException("Payment with ID " + paymentId + " doesn't exist");
//        }

        try {
            System.out.println(emitters.get(paymentId));
            emitters.get(paymentId)
                    .send(SseEmitter.event()
                            .name("yapay-confirm-payment")
                            .data("here goes a confirmed payment"));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @GetMapping("/payments/isconfirmed")
    public Boolean isconfirmPayment(@RequestHeader("paymentId") Long paymentId)
            throws IllegalArgumentException{
        /* Throws exception if payment ID wasn't
         * specified or doesn't exist */

        if (paymentId==null) {
            throw new IllegalArgumentException("Missing Payment ID");
        }

        Payment paymentToVerify;
        // TODO: Find payment in database
        //       If payment doesn't exist: throw error

        return true; //paymentToVerify.isConfirmed();
    }
}
