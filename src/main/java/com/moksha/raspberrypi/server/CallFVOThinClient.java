package com.moksha.raspberrypi.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moksha.raspberrypi.server.models.entities.Product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CallFVOThinClient {

    String HOST = "http://35.231.5.148:20000";

    String API_GET_ACTIONS = "";
    String API_POST_RESULT = "";

    OkHttpClient client ;

    public CallFVOThinClient() {
        client = new OkHttpClient();
    }

    public List<Product> getActions() throws IOException {
        List<Product> listOfProduct = new ArrayList<>();
        String url = HOST + API_GET_ACTIONS ;
//        System.out.println(url);
        Request request = new Request.Builder()
                .url(url)
                .build();
        String responseBody = null;
        try (Response response = client.newCall(request).execute()) {
            responseBody = response.body().string();
        }
//        System.out.println(responseBody);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);;
            if (jsonNode != null) {
                JsonNode produts = jsonNode.get("RESPONSE").get("products").get("listing-ids");
                if(produts.isArray())
                {
                    Iterator<JsonNode> iterator = produts.iterator();
                    while(iterator.hasNext())
                    {
                        JsonNode next = iterator.next();
                        System.out.printf("i");
                        String productId = next.get("product-id").asText();
                        String listingId = next.get("listing-id").asText();
                        listOfProduct.add(new Product(productId, listingId));
                    }
                }
            }
        }
        catch (NullPointerException ne)
        {
            System.out.printf("error"+ ne);
            return listOfProduct;
        }
        System.out.printf("bye"+ listOfProduct.size());
        return listOfProduct;
    }
}
