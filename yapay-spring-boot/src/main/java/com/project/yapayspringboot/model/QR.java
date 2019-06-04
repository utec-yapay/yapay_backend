package com.project.yapayspringboot.model;

import java.util.UUID;
import java.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.json.simple.JSONObject;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

public class QR {
    // TODO: Replace System.out.println's with logger

    private static void generateQR() throws WriterException, IOException {
        int height = 500;
        int width = 700;
        String filePath = "./qr_example.png";

        /* Create dummy json */
        JSONObject dummy_json = new JSONObject();
        dummy_json.put("paymentId", UUID.randomUUID());
        dummy_json.put("companyId", UUID.randomUUID());
        dummy_json.put("companyName", "Dodo Attack SA");
        dummy_json.put("amount", new Float(54.80));
        StringWriter text = new StringWriter();
        dummy_json.writeJSONString(text);

        /* Encode QR into bit matrix */
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text.toString(), BarcodeFormat.QR_CODE, width, height);

        /* Bit matrix to image in png file */
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

        /* Bit matrix to Base64 string */
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", out);
        String qrCodeString = "data:image/png;base64," + Base64.getEncoder().encodeToString(out.toByteArray());

        System.out.println(qrCodeString);
    }

    public static void main(String[] args){
        try {
            generateQR();
        }
        catch (Exception e){
            System.out.println("There was an error. " + e.getMessage());
        }
    }
}
