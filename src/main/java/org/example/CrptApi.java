package org.example;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.*;

import com.google.gson.Gson;
import lombok.Data;

public class CrptApi {
    private final Semaphore semaphore;
    private final ScheduledExecutorService scheduler;
    private final String API_URL = "https://ismp.crpt.ru/api/v3/lk/documents/create";

    //private static final Logger log = LoggerFactory.getLogger(CrptApi.class);

    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        this.semaphore = new Semaphore(requestLimit);
        this.scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(this::resetSemaphore, 0, timeUnit.toSeconds(1), TimeUnit.SECONDS);
    }

    public void createDocument(Object document, String signature) {
        try {
            semaphore.acquire();
            sendPostRequest(document, signature);
            //log.debug("Document created successfully.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            //log.error("Error creating document: " + e.getMessage());
        } finally {
            semaphore.release();
        }
    }

    private void sendPostRequest(Object document, String signature) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            Gson gson = new Gson();
            String jsonBody = gson.toJson(document);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            //log.debug("API Response Code: " + responseCode);

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetSemaphore() {
        semaphore.drainPermits();
        //log.error("Request limit reset.");
    }

    public void shutdown() {
        scheduler.shutdown();
    }

    @Data
    static class DocumentRequest {
        private Description description;
        private String doc_id;
        private String doc_status;
        private String doc_type;
        private boolean importRequest;
        private String owner_inn;
        private String participant_inn;
        private String producer_inn;
        private String production_date;
        private String production_type;
        private List<Product> products;
        private String reg_date;
        private String reg_number;
    }

    @Data
    static class Description {
        private String participantInn;

        public Description(String participantInn) {
            this.participantInn = participantInn;
        }
    }

    @Data
    static class Product {
        private String certificate_document;
        private String certificate_document_date;
        private String certificate_document_number;
        private String owner_inn;
        private String producer_inn;
        private String production_date;
        private String tnved_code;
        private String uit_code;
        private String uitu_code;
    }
}
