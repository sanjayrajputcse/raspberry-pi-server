package com.moksha.raspberrypi.server.fkService;

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

/**
 * Created by somil.jain on 30/05/19.
 */
public class SearchService {

    String HOST = "http://10.47.1.159:25290";
    String API ="/sherlock/stores/all/products?geoBrowse=enabled&groupingImpl=siblings&includeBuyability=true&products.count=5&products.type=listing&select.substores=off&serviceabilityCheckEnabled=true&pincode=560102&q=";
    OkHttpClient client ;

    public SearchService() {
        client = new OkHttpClient();
    }

    public List<Product> searchOnAKeyword(String word) throws IOException {
        List<Product> listOfProduct = new ArrayList<>();
        String url = HOST + API + word;
//        System.out.println(url);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-MARKETPLACE-CONTEXT","GROCERY")
                .addHeader("X-SERVICEABLE-MARKETPLACES" ,"FLIPKART,GROCERY")
                .build();
        String responseBody = null;
        try (Response response = client.newCall(request).execute()) {
           responseBody = response.body().string();
        }
//        System.out.println(responseBody);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);
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
                        Product product = new Product(productId, listingId);
                        product.setProductTitle(new ProductDetailService().getProductTitle(productId));
                        listOfProduct.add(product);
                        break;
                    }
                }
            }
        }
        catch (NullPointerException ne)
        {
            System.out.printf("error"+ ne);
        }
        System.out.printf("list of products " + listOfProduct.size());
        return listOfProduct;
    }


}
