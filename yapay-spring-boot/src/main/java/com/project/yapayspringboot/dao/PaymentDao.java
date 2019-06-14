package com.project.yapayspringboot.dao;

import com.project.yapayspringboot.model.Payment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

@Repository
public class PaymentDao {
    public PaymentDao(JdbcTemplate template) {
        this.template = template;
    }

    JdbcTemplate template;

    public List<Payment> selectAllPayments() {
        /* Returns all the payments in the database
        * as a Payment List */


        return template.query("SELECT * FROM payments",
                (rs, rowNum) -> {
                    Payment payment = new Payment(
                            rs.getString("company_name"),
                            rs.getString("company_phone"),
                            rs.getFloat("total")
                    );
                    payment.setConfirmed(rs.getBoolean("confirmed"));
                    payment.setId(rs.getLong("payment_id"));
                    return payment;
                }
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
                "INSERT INTO payments(company_phone, company_name, total) VALUES (?,?,?)",
                        Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1,payment.getCompany().getPhone());
                    ps.setString(2,payment.getCompany().getName());
                    ps.setFloat(3, payment.getTotalAmount());

                    return ps;
            }
        , keyHolder);

        return keyHolder.getKeys();
    }

    public Payment selectPaymentById(Long paymentId){
        /* Returns payment with id paymentId */

        return template.queryForObject(
                "SELECT * FROM payments WHERE payment_id = ?",
                (rs, rowNum) -> {
                     Payment payment = new Payment(
                            rs.getString("company_name"),
                            rs.getString("company_phone"),
                            rs.getFloat("total")
                    );
                    payment.setConfirmed(rs.getBoolean("confirmed"));
                    payment.setId(rs.getLong("payment_id"));
                    return payment;
                },
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
}