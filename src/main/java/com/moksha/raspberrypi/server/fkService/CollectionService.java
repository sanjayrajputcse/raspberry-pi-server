package com.moksha.raspberrypi.server.fkService;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moksha.raspberrypi.server.models.entities.CollectionRequest;
import com.moksha.raspberrypi.server.models.entities.CollectionResponse;
import okhttp3.*;

import java.io.IOException;

/**
 * Created by somil.jain on 31/05/19.
 */
public class CollectionService {

    String METHOD_GET = "GET";
    String METHOD_POST = "POST";
    String HOST = "http://10.47.7.31";
    String CREATE_API = "/collection-service/v0/collection";

    OkHttpClient client ;
    ObjectMapper objectMapper;

    public CollectionService() {

        client = new OkHttpClient();
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    public CollectionResponse createNewCollection(CollectionRequest collectionRequest) throws IOException {

        String url = HOST + CREATE_API;
        String request = objectMapper.writeValueAsString(collectionRequest);
        String response = post(url, request);
        return objectMapper.readValue(response,CollectionResponse.class);
    }

    String post(String url, String json) throws IOException {
        final MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("X_TEAM_ID","LBHAV2UYG8")
                .addHeader("X_USER_ID","pushkar.anand@flipkart.com")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
