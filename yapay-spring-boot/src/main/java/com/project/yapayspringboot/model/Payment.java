package com.project.yapayspringboot.model;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

public class Payment {
    private Long id;
    private Company company;
    private float totalAmount;
    private boolean confirmed = false;

    public Payment(String companyName, String companyPhone, float amount) {
        company = new Company(companyName, companyPhone);
        totalAmount = amount;
        // TODO: Add to database
        // id = id in data base
    }

    public String generateJwt(){
        // TODO: Generate JWT
        final Instant now = Instant.now();

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("secret");
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        // TMP: return actual generated jwt
        return Jwts.builder()
                        .setIssuedAt(Date.from(now))
                        .setExpiration(Date.from(now.plus(15, ChronoUnit.SECONDS)))
                        .claim("pid", this.id)
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
