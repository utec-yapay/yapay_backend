package com.project.yapayspringboot.controller;

import com.project.yapayspringboot.model.Payment;

import com.project.yapayspringboot.service.PaymentService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.io.IOException;
import java.util.HashMap;

@RestController
@CrossOrigin
public class PaymentController {
    private HashMap<Long, SseEmitter> emitters = new HashMap<>();
    // TODO: Error handling for socket

    @Resource
    PaymentService paymentService;

    @GetMapping("/payments")
    public ResponseEntity<List<Payment>> getAllPayments(){
        return new ResponseEntity<>(paymentService.getAllPayments(), HttpStatus.OK);
    }

    @RequestMapping("/confirmEvent/{id}")
    public SseEmitter confirmEvent(@PathVariable(value="id") Long id) {
        long timeout = 60000L;
        SseEmitter sseEmitter = new SseEmitter(timeout); // 15s
        emitters.put(id, sseEmitter);
        System.out.println("Connection established with client. Now processing payment with id: " + id +
                ". Time remaining: " + (timeout/1000) + " secs");
        sseEmitter.onCompletion(() -> emitters.remove(id));
        return sseEmitter;
    }

    @PostMapping("/payments")
    public ResponseEntity<String> createPayment(@Valid @RequestBody Payment newpayment){

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

        Long id = paymentService.addPayment(newpayment);
        newpayment.setId(id);

        return new ResponseEntity<>(newpayment.generateJwt(), HttpStatus.OK);
    }

    @GetMapping("/payments/confirm")
    public synchronized ResponseEntity confirmPayment(@RequestHeader("pid") Long paymentId){

        /* Confirms payment with id paymentId and
         * inserts payment to database. Sends signal
         * to the UI.
         *
         * Throws exception if payment ID wasn't
         * specified, doesn't exist or is already
         * confirmed  */


        if (paymentId==null){
            return new ResponseEntity<>("Missing Payment ID", HttpStatus.BAD_REQUEST);
        }

        Payment paymentToConfirm;

        // Gets payment with id payment id from database
        try {
            paymentToConfirm = paymentService.getPaymentById(paymentId);
        } catch (EmptyResultDataAccessException e){
            return new ResponseEntity<>("Payment with ID " + paymentId + " doesn't exist", HttpStatus.BAD_REQUEST);
        }



        // Verify if payment was already confirmed and change paymentToConfirm.confirmed
        if (!paymentToConfirm.confirm()){
            try {
                emitters.get(paymentId)
                        .send(SseEmitter.event()
                                .name("yapay-confirm-payment")
                                .data("Payment with id " + paymentId + " confirmed."));
                emitters.get(paymentId).complete();
                System.out.println("Confirmation of payment with id: " + paymentId + " completed. Connection closed");
                return new ResponseEntity<>("Payment with ID " + paymentId + " is already confirmed", HttpStatus.BAD_REQUEST);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>("Payment with ID " + paymentId + " is already confirmed", HttpStatus.BAD_REQUEST);
            }
        }

        // Insert confirmed into db
        paymentService.updateConfirmation(paymentToConfirm);

        // Sends confirmed payment signal to UI
        try {
            emitters.get(paymentId)
                    .send(SseEmitter.event()
                            .name("yapay-confirm-payment")
                            .data("Payment with id " + paymentId + " confirmed."));
            emitters.get(paymentId).complete();
            System.out.println("Confirmation of payment with id: " + paymentId + " completed. Connection closed");
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(false, HttpStatus.OK);

    }

    /*
    DEPRECATED

    @GetMapping("/payments/isconfirmed")
    public Boolean isconfirmPayment(@RequestHeader("paymentId") Long paymentId) throws IllegalArgumentException{
//        Throws exception if payment ID wasn't
//         specified or doesn't exist

        if (paymentId==null) {
            throw new IllegalArgumentException("Missing Payment ID");
        }

        Payment paymentToVerify;
        try {
            paymentToVerify = paymentService.getPaymentById(paymentId);
        }catch (EmptyResultDataAccessException e){
            throw new IllegalArgumentException("Payment with ID" + paymentId + " doesn't exist");
        }

        return true; //paymentToVerify.isConfirmed();
    }

     */
}
