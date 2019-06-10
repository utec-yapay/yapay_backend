package com.project.yapayspringboot.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class Payment {
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
        final Instant now = Instant.now();

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // TODO: Put this in a file
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("KbPeShVmYq3t6w9z$C&F)H@McQfTjWnZ-KbPeShVmYq3t6w9z$C&F)H@McQfTjWnZ-KbPeShVmYq3t6w9z$C&F)H@McQfTjWnZ-KbPeShVmYq3t6w9z$C&F)H@McQfTjWnZ");
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        return Jwts.builder()
                        .setIssuedAt(Date.from(now))
                        .setExpiration(Date.from(now.plus(60, ChronoUnit.SECONDS)))
                        .claim("pid", this.id)
                        .claim("amt", this.totalAmount)
                        .claim("cpn", this.company.getName())
                        .claim("cpp", this.company.getPhone())
                        .signWith(signatureAlgorithm, signingKey)
        .compact();
    }



    public Boolean confirm(){
        if (confirmed) return false;
        return confirmed = true;
    }

    public void setId(Long id) { this.id = id; }
    public void setConfirmed(boolean confirmed) { this.confirmed = confirmed; }

    public Float getTotalAmount() { return totalAmount; }
    public Company getCompany() { return company; }
    public Long getId(){ return id; }
    public Boolean isConfirmed() { return confirmed; }


}