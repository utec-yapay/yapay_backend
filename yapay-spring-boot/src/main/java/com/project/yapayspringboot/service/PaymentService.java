package com.project.yapayspringboot.service;

import com.project.yapayspringboot.dao.PaymentDao;
import com.project.yapayspringboot.model.Payment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PaymentService {
    @Resource
    PaymentDao paymentDao;

    public Long addPayment(Payment payment){
        return (Long) paymentDao.insertPayment(payment).get("payment_id");
    }

    public Payment getPaymentById(Long paymentId){
        return paymentDao.selectPaymentById(paymentId);
    }

    public void updateConfirmation(Payment payment){
        paymentDao.updatePayment(payment);
    }

    public List<Payment> getYesterdayConfirmedPayments(){ return paymentDao.selectYesterdayConfirmedPayments(); }

    public List<Payment> getAllPayments(){
        return paymentDao.selectAllPayments();
    }

}
