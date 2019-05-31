package com.moksha.raspberrypi.server.fkService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.jdbc.StringUtils;
import lombok.Data;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created by somil.jain on 31/05/19.
 */

@Data
public class ProductDetailService {
    String HOST = "http://10.47.1.8:31200";
    String API = "/views?viewNames=product_base_info&entityIds=";

    OkHttpClient client ;

    public ProductDetailService() {
        this.client = new OkHttpClient();
    }

    public String getProductTitle(String productId) throws IOException {
        if (StringUtils.isNullOrEmpty(productId)) {
            return null;
        }
        String url = HOST + API + productId;
        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-MARKETPLACE-CONTEXT", "GROCERY")
                .addHeader("X-SERVICEABLE-MARKETPLACES", "FLIPKART,GROCERY")
                .addHeader("z-clientId", "w3.merch")
                .addHeader("z-timestamp", "00:00:00")
                .addHeader("z-requestId", "1234")
                .build();
        String responseBody = null;
        try (Response response = client.newCall(request).execute()) {
            responseBody = response.body().string();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
//            String title = jsonNode.get("entityViews").get(0)
//                    .get("view").get("titles").get("view_title").get("title").asText();
            String title = jsonNode.get("entityViews").get(0)
                    .get("view").get("titles").get("w3_title").asText();
            return title;
        }
        catch (Exception e)
        {
            System.out.println("Unable to fetch product details for productId : "+ productId);
            return null;
        }

    }
}
