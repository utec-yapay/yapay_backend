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
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(" -----BEGIN RSA PRIVATE KEY-----\n" +
                "MIIEpQIBAAKCAQEA3+CvPvZsIAD8BC1OuAsidX7rHOUyQ4w/G+rfsO1ROClMwb/F\n" +
                "9ULy8zSvx8OqfXC5aPaB8WuTZ8Xk0VS1/Z2UB0Hx5z7/EZPXiva77aSJuJIKkm4V\n" +
                "3xmGA8R6RbhkmAOXaJFffeRgyB+SjfiB9SQTiztou8GFoytK2SE0cA7QsQ3VgvOg\n" +
                "+h3BVA9/KRsxL1pXKV8kBntuz6cduft7st530HeUMvM4XO4vmYwLvP+cPrCkcey2\n" +
                "jFYJiq/37yzD/pe9pq6UJFA01Vde8f1FGZFxZ2rY5KL5JGrEI3QRZ6jmM9IQ2z6S\n" +
                "wjUFy75kD1AvZRt5sK/GVYq19g36d3Ko4AlYvQIDAQABAoIBAQCgaS9oH80NiWcJ\n" +
                "3yTePiwsoAn6pEbFm4HEkSBCd2iQoxb6ZFyFBblqhb/FlO9d/hz7llU5FGmEG4l+\n" +
                "bAISfIwKPzgDB2Jfx9zTF0NEXXbIpuuoS/Sj7HfXzzoFJr/9I+Wi6TbQz4iBHA44\n" +
                "Z21GptjSFHlQvrLwXR6+QUp/WD4z/WyB35OGN4ztMxxDK0Km7iCc/JE+rsoxHssx\n" +
                "Ah6SRXuLZ83r5ZYt98FVYY6Nknrikbv/VVxpbMpbUHu7dZu/2N47dcvNyMvF+BnS\n" +
                "72vnnUDhwUfpHilg5Lu7l7gjQZ6WPKvkih+XQ1H71QPIarvIfKBJVXnMz9xiczXn\n" +
                "GzwYpVtJAoGBAPBW9pZnH/5XArSuL8mRquWsxzPPMCZNL/C2HITbJi8HbYqMXfaI\n" +
                "qXOBLnvvYp0EB/DPdhAo399gIcz3STGGqi/UTjKheKgQGDwBceapFJeY0WCEpT+a\n" +
                "0lWe3A3GOiYWV3z6hW5P2hddJiSgUp89fzCV2CVDr7xoIRSfL4F7WiJ/AoGBAO53\n" +
                "H3/MHncsXs8XO5QAo9VuhZCekg1cAQZ71V8eyaS6f2e3M9MFT+ocsHMORoUbFFj0\n" +
                "2k7c6yBR5qdgP6T5dN1AP98ACsElZKUp6T4EoBZ3w6Vvtp8JXEN4PkfvUQ+7di7R\n" +
                "ogQAa8f7ALQ+jhByqsAHLIcH7Jwkw8HtaJx8Me7DAoGANKSnwIv4GwrOAeuBdiJd\n" +
                "D4/H1lZUkp8nmA8bshIajASRft5+GmkWzMEIAIePzxq57opSrvl1CAWTgcTMmHeJ\n" +
                "wY48TqTFu/JCjKo4W7C/XPFRM1X6qDLuTWjNhIrd48fTBAIPxfjhqWpy12TylASx\n" +
                "XmAEBy2LSbZ5QH4Ztr/hH1sCgYEAgWEGKQsw+E2NfuRHAcy7FuBo/QbbjP3+3Gqx\n" +
                "YHNOyd4Zo8blCjeWnRlFrpbvMeZ4Sq9GBoWb2CQ3dVYmEbb6bdQfEzltnE9SZL3x\n" +
                "eyF9TdNaxdZviafFdCG4AMVaAKQfwdUhBvTHhW+seR57a3cbJyZ9RSHKgQj9YKkT\n" +
                "GdvUJ4sCgYEA0Usu/NNDn4NlwI+FLontbUPAVcx6KJLkm/IfVyQ6LVHzFn7KKypk\n" +
                "7DCBYk2ocIZc0dFuAHi7JnjChkbQ0c53EChorx53ssxC9yewNrv0puSE9zlJZCIO\n" +
                "5FUqoZ1ZfKc3DyBgAOxkPUy1fw5zAzDfPGvCvoki6/sV/+cM/cC+I38=\n" +
                "-----END RSA PRIVATE KEY-----");
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

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
