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
    }

    public String generateJwt(){
        String secret = "2r5u8x/A?D(G-KaPdSgVkYp3s6v9y$B&";
        String token = "";

        try {
            Algorithm algorithmHS = Algorithm.HMAC256(secret);

            Calendar inOneMin = Calendar.getInstance();
            inOneMin.add(Calendar.MINUTE, 1);

            token = JWT.create()
                .withClaim("pid", 123)
                .withClaim("cpn", "Drimer")
                .withClaim("cpp", "993321323")
                .withExpiresAt(inOneMin.getTime())
                .sign(algorithmHS);
        } catch (UnsupportedEncodingException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        Algorithm algorithm = null;
        try {
            algorithm = Algorithm.HMAC256(secret);
        } catch (UnsupportedEncodingException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        try {
            JWTVerifier verifier = JWT.require(algorithm).build(); //Reusable verifier instance
            verifier.verify(token);
            logger.log(Level.FINE, "Valid JWT");
        } catch (JWTVerificationException exception){
            logger.log(Level.SEVERE, "Invalid JWT");
        }

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