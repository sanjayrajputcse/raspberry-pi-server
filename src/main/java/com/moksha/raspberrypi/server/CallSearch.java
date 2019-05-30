package com.moksha.raspberrypi.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moksha.raspberrypi.server.models.entities.Product;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by somil.jain on 30/05/19.
 */
public class CallSearch {

    String HOST = "http://10.47.1.159:25290";
    String API ="/sherlock/stores/all/products?geoBrowse=enabled&groupingImpl=siblings&includeBuyability=true&products.count=5&products.type=listing&select.substores=off&serviceabilityCheckEnabled=true&pincode=560102&q=";
    OkHttpClient client ;

    public CallSearch() {
        client = new OkHttpClient();
    }

    public List<Product> searchOnAKeyword(String word) throws IOException {
        List<Product> listOfProduct = new ArrayList<>();
        String url = HOST + API ;
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
