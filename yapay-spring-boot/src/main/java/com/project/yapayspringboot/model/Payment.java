package com.project.yapayspringboot.model;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
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
    private Double totalAmount;
    private Boolean confirmed = false;
    private LocalDateTime creation_date = LocalDateTime.now().minus(1, ChronoUnit.DAYS);


    public static class Token{
        static private Algorithm algorithm;

        static{
            // Initialize algorithm
            String secret = "2r5u8x/A?D(G-KaPdSgVkYp3s6v9y$B&";
            try {
                algorithm = Algorithm.HMAC256(secret);
                logger.log(Level.FINE, "Algorithm for JWT was created");
            } catch(UnsupportedEncodingException e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
            }
        }

        static public Boolean validateToken(String token){
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

        static public Claim getClaim(String token, String key){
            return JWT.decode(token).getClaim(key);
        }

        static private String generateToken(Long id, Company company, Double totalAmount){
            Calendar inOneMin = Calendar.getInstance();
            inOneMin.add(Calendar.MINUTE, 1);

            return JWT.create()
                    .withClaim("pid", id)
                    .withClaim("cpn", company.getName())
                    .withClaim("cpp", company.getPhone())
                    .withClaim("amt", totalAmount)
                    .withExpiresAt(inOneMin.getTime())
                    .sign(algorithm);
        }
    }

    public Payment(@JsonProperty("cpn") String companyName,
                   @JsonProperty("cpp") String companyPhone,
                   @JsonProperty("amt") Double amount) {
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
        return Token.generateToken(id, company, totalAmount);
    }

    public Boolean confirm(){
        if (confirmed) return false;
        confirmed = true;
        return confirmed;
    }

    public void setId(Long id) { this.id = id; }
    public void setConfirmed(boolean confirmed) { this.confirmed = confirmed; }
    public void setCreationDate(LocalDateTime creation_date) { this.creation_date = creation_date; }

    public Double getTotalAmount() { return totalAmount; }
    public Company getCompany() { return company; }
    public Long getId(){ return id; }
    public Boolean isConfirmed() { return confirmed; }
    public LocalDateTime getCreationDate() { return creation_date; }



}