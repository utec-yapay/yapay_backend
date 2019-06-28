package com.project.yapayspringboot.dao;

import com.project.yapayspringboot.controller.PaymentController;
import com.project.yapayspringboot.model.Payment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Null;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class PaymentDao {
    private static final Logger logger = Logger.getLogger(PaymentDao.class.getName());
    public PaymentDao(JdbcTemplate template) {
        this.template = template;
    }

    JdbcTemplate template;

    private Payment databaseToPayment(ResultSet rs, int rowNum){
        Payment payment;
        try{
            payment = new Payment(
                    rs.getString("company_name"),
                    rs.getString("company_phone"),
                    rs.getDouble("total")
            );
            payment.setConfirmed(rs.getBoolean("confirmed"));
            payment.setId(rs.getLong("payment_id"));
            payment.setCreationDate(rs.getObject("creation_date", LocalDateTime.class));
        }catch (SQLException e){
            logger.log(Level.SEVERE, e.toString(), e);
            return null;
        }
        logger.log(Level.FINE, "Payment created from database row");
        return payment;
    }

    public List<Payment> selectAllPayments() {
        /* Returns all the payments in the database
        * as a Payment List */


        return template.query("SELECT * FROM payments",
                this::databaseToPayment
        );
        // Logger: .forEach(customer -> log.info(customer.toString()))
    }

    public Map<String,Object> insertPayment(Payment payment){
        /* Inserts a Payment, taking the companyPhone,
        * companyName and totalAmount. The database
        * automatically sets the id and creation_date
        *
        * Returns the id of the inserted payment */


        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO payments(company_phone, company_name, total, creation_date) VALUES (?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1,payment.getCompany().getPhone());
                    ps.setString(2,payment.getCompany().getName());
                    ps.setDouble(3, payment.getTotalAmount());
                    ps.setTimestamp(4, Timestamp.valueOf(payment.getCreationDate()));

                    return ps;
            }
        , keyHolder);

        return keyHolder.getKeys();
    }

    public Payment selectPaymentById(Long paymentId){
        /* Returns payment with id paymentId */

        return template.queryForObject(
                "SELECT * FROM payments WHERE payment_id = ?",
                this::databaseToPayment,
                paymentId
        );
    }

    public void updatePayment(Payment payment){
        /* Updates all columns of payment */

        template.update(
            "UPDATE payments " +
                  "SET company_name=?," +
                      "company_phone=?," +
                      "total=?," +
                      "confirmed=? " +
                  "WHERE payment_id=?",
            payment.getCompany().getName(),
            payment.getCompany().getPhone(),
            payment.getTotalAmount(),
            payment.isConfirmed(),
            payment.getId()
        );
    }

    public List<Payment> selectYesterdayConfirmedPayments(){
        return template.query("SELECT * FROM payments WHERE creation_date::DATE = CURRENT_DATE - 1 AND confirmed=TRUE",
                this::databaseToPayment
        );
    }
}