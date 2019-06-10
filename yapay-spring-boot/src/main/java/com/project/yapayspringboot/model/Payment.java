package com.project.yapayspringboot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

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

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // TODO: Put this in a file
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("KbPeShVmYq3t6w9z$C&F)H@McQfTjWnZ-KbPeShVmYq3t6w9z$C&F)H@McQfTjWnZ-KbPeShVmYq3t6w9z$C&F)H@McQfTjWnZ-KbPeShVmYq3t6w9z$C&F)H@McQfTjWnZ");
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        System.out.println("format: " + signingKey.getFormat());
        System.out.println("encoded: " + Arrays.toString(signingKey.getEncoded()));
        // TMP: return actual generated jwt
        return Jwts.builder()
                        .setIssuedAt(Date.from(now))
                        .setExpiration(Date.from(now.plus(15, ChronoUnit.SECONDS)))
                        .claim("pid", 123)
                        .claim("amt", this.totalAmount)
                        .claim("cpn", this.company.getName())
                        .claim("cpp", this.company.getPhone())
                        .signWith(signatureAlgorithm, signingKey)
        .compact();
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
