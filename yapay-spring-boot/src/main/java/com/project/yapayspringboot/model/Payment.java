package com.project.yapayspringboot.model;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.UnsupportedEncodingException;
import java.time.Instant;

public class Payment {
    private Long id;
    private Company company;
    private float totalAmount;
    private boolean confirmed = false;

    public Payment(@JsonProperty("cpn") String companyName,
                   @JsonProperty("cpp") String companyPhone,
                   @JsonProperty("amt") float amount) {
        this.company = new Company(companyName, companyPhone);
        this.totalAmount = amount;
        // TODO: Add to database
        // id = id in data base
    }

    public String generateJwt(){
        final Instant now = Instant.now();

        // TODO: Put this in a file
        String secret = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
        String token = "";

        try {
            Algorithm algorithmHS = Algorithm.HMAC256(secret);
            token = JWT.create()
                .withClaim("pid", 123)
                .withClaim("cpn", "Drimer")
                .withClaim("cpp", "993321323")
                .sign(algorithmHS);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            Algorithm algorithm = null;
            try {
                algorithm = Algorithm.HMAC256(secret);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            JWTVerifier verifier = JWT.require(algorithm)
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            System.out.println("VALID JWT");
        } catch (JWTVerificationException exception){
            System.out.println("INVALID JWT");
        }

        return token;
    }

    public boolean confirm(){
        if (confirmed) return false;
        confirmed = true;

        // TODO: Find payment in database and update confirmed attribute


        return true;
    }

    public Long getId(){ return id; }
    public boolean isConfirmed() { return confirmed; }
}
