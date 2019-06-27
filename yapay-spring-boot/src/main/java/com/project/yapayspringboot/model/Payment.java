package com.project.yapayspringboot.model;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class Payment {
    private static final Logger logger = Logger.getLogger(Payment.class.getName());

    private Long id;
    @NotNull
    @Valid
    private Company company;
    @NotNull
    private Float totalAmount;
    private Boolean confirmed = false;
    static Algorithm algorithm;

    public Payment(@JsonProperty("cpn") String companyName,
                   @JsonProperty("cpp") String companyPhone,
                   @JsonProperty("amt") float amount) {
        /* This constructor is used when creating a
        *  new payment.
        *  The controller inserts the object to the
        *  database, which returns an id. Then, it
        * sets the payment id */

        this.company = new Company(companyName, companyPhone);
        this.totalAmount = amount;
        confirmed = false;

        // Generate JWT algorithm
        try {
            String jwtSecret = "2r5u8x/A?D(G-KaPdSgVkYp3s6v9y$B&";
            algorithm = Algorithm.HMAC256(jwtSecret);
        } catch (UnsupportedEncodingException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static Boolean validateJwt(String token){
        try {
            JWTVerifier verifier = JWT.require(algorithm).build(); //Reusable verifier instance
            verifier.verify(token);
            logger.log(Level.FINER, "Valid JWT");
        } catch (JWTVerificationException exception){
            logger.log(Level.FINER, "Invalid JWT");
            return false;
        }
        return true;
    }

    public String generateJwt(){
        String token = "";

        Calendar inOneMin = Calendar.getInstance();
        inOneMin.add(Calendar.MINUTE, 1);

        token = JWT.create()
            .withClaim("pid", this.id)
            .withClaim("cpn", this.company.getName())
            .withClaim("cpp", this.company.getPhone())
            .withExpiresAt(inOneMin.getTime())
            .sign(algorithm);

        return token;
    }


    public Boolean confirm(){
        if (confirmed) return false;
        confirmed = true;
        return confirmed;
    }

    public void setId(Long id) { this.id = id; }
    public void setConfirmed(boolean confirmed) { this.confirmed = confirmed; }

    public Float getTotalAmount() { return totalAmount; }
    public Company getCompany() { return company; }
    public Long getId(){ return id; }
    public Boolean isConfirmed() { return confirmed; }


}