package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.example.CrptApi.*;

public class App 
{
    public static void main(String[] args) {
        CrptApi crptApi = new CrptApi(TimeUnit.SECONDS, 4);

        DocumentRequest documentRequest = new DocumentRequest();
        documentRequest.setDescription(new Description("string"));
        documentRequest.setDoc_id("string");
        documentRequest.setDoc_status("string");
        documentRequest.setDoc_type("LP_INTRODUCE_GOODS");
        documentRequest.setImportRequest(true);
        documentRequest.setOwner_inn("string");
        documentRequest.setParticipant_inn("string");
        documentRequest.setProducer_inn("string");
        documentRequest.setProduction_date("2020-01-23");
        documentRequest.setProduction_type("string");

        Product product = new Product();
        product.setCertificate_document("string");
        product.setCertificate_document_date("2020-01-23");
        product.setCertificate_document_number("string");
        product.setOwner_inn("string");
        product.setProducer_inn("string");
        product.setProduction_date("2020-01-23");
        product.setTnved_code("string");
        product.setUit_code("string");
        product.setUitu_code("string");
        List<Product> products = new ArrayList<>();
        documentRequest.setProducts(products);

        documentRequest.setReg_date("2020-01-23");
        documentRequest.setReg_number("string");

        crptApi.createDocument(documentRequest, "signature");
        crptApi.shutdown();
    }
}
