package com.project.yapayspringboot.model;

import com.google.gson.Gson;
import com.project.yapayspringboot.controller.PaymentController;
import com.project.yapayspringboot.service.PaymentService;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class DataReportScheduler {
    private static final Logger logger = Logger.getLogger(DataReportScheduler.class.getName());

    @Resource
    PaymentService paymentService;

    private String handlerUrl = "http://yape_handler/handler/payments";


    // cron expressions: second, minute, hour, day, month, weekday
    @Scheduled(cron = "0 0 0 * * *") // midnight everyday
//    @Scheduled(fixedRate = 20000) // For debugging only
    public void sendDataReport(){
        DataReport dataReport = new DataReport();
        dataReport.setPayments(paymentService.getYesterdayConfirmedPayments());

        // Get dataReport serialized as JSON
        Gson gson = new Gson();
        StringEntity dataReportJson;
        try {
            dataReportJson = new StringEntity(gson.toJson(dataReport));
        } catch (UnsupportedEncodingException e){
            logger.log(Level.SEVERE, e.toString(), e);
            return;
        }

        // Send POST to Yape handler
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(this.handlerUrl);
        post.setEntity(dataReportJson);

        logger.log(Level.FINE, "Data report is being sent");
        try {
            if (httpClient.execute(post).getStatusLine().getStatusCode()!=200){
                logger.log(Level.FINE, "Data report sent successfully");
            }
        }catch (IOException e){
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }
}
