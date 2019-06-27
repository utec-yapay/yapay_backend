package com.project.yapayspringboot.controller;

import com.auth0.jwt.JWT;
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
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@CrossOrigin // Must be a specific address in production
public class PaymentController {
    private static final Logger logger = Logger.getLogger(PaymentController.class.getName());

    private HashMap<Long, SseEmitter> emitters = new HashMap<>();

    @Resource
    PaymentService paymentService;

    @GetMapping("/payments")
    public ResponseEntity<List<Payment>> getAllPayments(){
        logger.log(Level.INFO, () -> "Get request made to /payments");
        return new ResponseEntity<>(paymentService.getAllPayments(), HttpStatus.OK);
    }

    @GetMapping("/confirmEvent/{id}")
    public SseEmitter confirmEvent(@PathVariable(value="id") Long id) {
        logger.log(Level.INFO, () -> "Request made to /confirmEvent/" + id);
        long timeout = 60000L;
        SseEmitter sseEmitter = new SseEmitter(timeout); // 15s
        emitters.put(id, sseEmitter);
        logger.log(Level.FINE, () -> "Connection established with client. Now processing payment with id: " + id +
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
        logger.log(Level.INFO, () -> "POST request made to /payments");
        logger.log(Level.INFO, () -> "Adding payment to database. Payment = " + newpayment);
        Long id = paymentService.addPayment(newpayment);
        newpayment.setId(id);
        logger.log(Level.FINE, () -> "Payment added to database with id = " + id);

        return new ResponseEntity<>(newpayment.generateJwt(), HttpStatus.OK);
    }

    @GetMapping("/payments/confirm")
    public synchronized ResponseEntity confirmPayment(@RequestHeader("pid") String paymentIdJwt){

        /* Confirms payment with id paymentId and
         * inserts payment to database. Sends signal
         * to the UI.
         *
         * Throws exception if payment ID wasn't
         * specified, doesn't exist or is already
         * confirmed  */

        logger.log(Level.INFO, () -> "GET request made to /payments/confirm");
        if (paymentIdJwt==null){
            logger.log(Level.SEVERE, () -> "Payment Id received in /payments/confirm is NULL");
            return new ResponseEntity<>("Missing JWT Payment ID", HttpStatus.BAD_REQUEST);
        }

        if (!Payment.validateJwt(paymentIdJwt)){
            return new ResponseEntity<>("JWT is expired", HttpStatus.BAD_REQUEST);
        }

        Long paymentId = JWT.decode(paymentIdJwt).getClaim("pid").asLong();

        Payment paymentToConfirm;

        // Gets payment with id payment id from database
        try {
            paymentToConfirm = paymentService.getPaymentById(paymentId);
        } catch (EmptyResultDataAccessException e){
            return new ResponseEntity<>("Payment ID doesn't exist", HttpStatus.BAD_REQUEST);
        }



        // Verify if payment was already confirmed and change paymentToConfirm.confirmed
        if (!paymentToConfirm.confirm()){
            try {
                emitters.get(paymentId)
                        .send(SseEmitter.event()
                                .name("yapay-confirm-payment")
                                .data("Payment with id " + paymentId + " confirmed."));
                emitters.get(paymentId).complete();
                logger.log(Level.FINE, () -> "Confirmation of payment with id: " + paymentId + " is completed. Connection closed");
                return new ResponseEntity<>("Payment is already confirmed", HttpStatus.BAD_REQUEST);
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
                return new ResponseEntity<>("Payment is already confirmed", HttpStatus.BAD_REQUEST);
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
            logger.log(Level.FINE, () -> "Confirmation of payment with id: " + paymentId + " is completed. Connection closed");
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        return new ResponseEntity<>(false, HttpStatus.OK);
    }

    @GetMapping("/payments/jwt")
    public synchronized ResponseEntity<String> generateUnexpiredJwt(@RequestHeader("pid") Long paymentId){
        /* Generates a JWT for a payment with a given
         * paymentId.
         *
         * Used when a JWT has expired and another JWT
         * is needed
         *
         * Throws exception if payment ID wasn't
         * specified or doesn't exist */

        if (paymentId==null){
            return new ResponseEntity<>("Missing Payment ID", HttpStatus.BAD_REQUEST);
        }

        Payment paymentToJwt;

        // Gets payment with id payment id from database
        try {
            paymentToJwt = paymentService.getPaymentById(paymentId);
        } catch (EmptyResultDataAccessException e){
            return new ResponseEntity<>("Payment ID doesn't exist", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(paymentToJwt.generateJwt(), HttpStatus.OK);

    }
}
