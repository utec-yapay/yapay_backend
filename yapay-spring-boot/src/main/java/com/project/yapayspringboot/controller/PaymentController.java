package com.project.yapayspringboot.controller;

import com.project.yapayspringboot.dao.PaymentDao;
import com.project.yapayspringboot.model.Payment;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
public class PaymentController {
    // TODO: Error handling

    @Resource
    PaymentDao paymentDao;

    @GetMapping("/payments")
    public List<Payment> getAllPayments(){
        return paymentDao.selectAllPayments();
    }

    @PostMapping("/payments")
    public String createPayment(@RequestBody Map<String, Object> payload) throws IllegalArgumentException{
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

        Payment newpayment;
        try{
            newpayment = new Payment((String) payload.get("companyName"),
                    (String) payload.get("companyPhone"),
                    new Float( (Double) payload.get("amount")));
        }catch (Exception e){
            throw new IllegalArgumentException("Invalid payment");
        }


        Long id = paymentDao.insertPayment(newpayment);
        newpayment.setId(id);

        return newpayment.generateJwt();
    }

    @GetMapping("/payments/confirm")
    public synchronized void confirmPayment(@RequestHeader("paymentId") Long paymentId) throws IllegalArgumentException{
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
        try {
            paymentToConfirm = paymentDao.selectPaymentById(paymentId);
        } catch (EmptyResultDataAccessException e){
            throw new IllegalArgumentException("Payment with ID" + paymentId + " doesn't exist");
        }

        if (!paymentToConfirm.confirm()){
            throw new IllegalArgumentException("Payment with ID " + paymentId + " is already confirmed");
        }

        paymentDao.updatePayment(paymentToConfirm);
    }

    @GetMapping("/payments/isconfirmed")
    public Boolean isconfirmPayment(@RequestHeader("paymentId") Long paymentId) throws IllegalArgumentException{
        /* Throws exception if payment ID wasn't
        * specified or doesn't exist */

        if (paymentId==null) {
            throw new IllegalArgumentException("Missing Payment ID");
        }

        Payment paymentToVerify;
        try {
            paymentToVerify = paymentDao.selectPaymentById(paymentId);
        }catch (EmptyResultDataAccessException e){
            throw new IllegalArgumentException("Payment with ID" + paymentId + " doesn't exist");
        }

        return paymentToVerify.isConfirmed();
    }

}
