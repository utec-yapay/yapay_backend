package com.project.yapayspringboot.model;

import java.util.UUID;
import java.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;

// import java.nio.file.FileSystems;
// import java.nio.file.Path;

import org.json.simple.JSONObject;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

public class QR {
    // TODO: Replace System.out.println's with logger

    private UUID id = UUID.randomUUID();
    private String qrcodeData;

    public QR(UUID paymentId, Company company, float amount) {

        try {
            qrcodeData = generateQR(paymentId, company, amount);
        }catch (Exception e){
            System.out.println("There was an error. " + e);
        }
    }

    static public String generateQR(UUID paymentId, Company company, Float amount) throws WriterException, IOException{
        int size = 500;

        /* Create json */
        JSONObject json = new JSONObject();
        json.put("paymentId", paymentId.toString());
        json.put("companyPhone", company.getPhone());
        json.put("companyName", company.getName());
        json.put("amount", amount);
        StringWriter jsonText = new StringWriter();
        json.writeJSONString(jsonText);

        /* Encode QR into bit matrix */
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(jsonText.toString(), BarcodeFormat.QR_CODE, size, size);

        /* Bit matrix to Base64 string */
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", out);

        return "data:image/png;base64," + Base64.getEncoder().encodeToString(out.toByteArray());

        /* Bit matrix to image in png file
        String filePath = "./example_qr.png";
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        */
    }

    public UUID getId(){ return id; }
    public String getQrData(){ return qrcodeData; }

    static public void main(String args[]){
        QR qr = new QR(UUID.randomUUID(), new Company("Dodo Attack SA", "993111295"), new Float(324.2));
        System.out.println(qr.getQrData());
    }

}
