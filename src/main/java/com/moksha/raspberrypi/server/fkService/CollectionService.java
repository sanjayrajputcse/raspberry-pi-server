package com.moksha.raspberrypi.server.fkService;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moksha.raspberrypi.server.models.entities.CollectionRequest;
import com.moksha.raspberrypi.server.models.entities.CollectionResponse;
import com.mysql.jdbc.StringUtils;
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
    String SUBMIT_API = "/collection-service/v0/collection/submit/";
    String UPDATE_API = "/collection-service/v0/collection/large-adhoc/";

    OkHttpClient client;
    ObjectMapper objectMapper;

    public CollectionService() throws IOException {

        client = new OkHttpClient();
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public CollectionResponse createNewCollection(CollectionRequest collectionRequest) throws IOException {

        String url = HOST + CREATE_API;
        String request = objectMapper.writeValueAsString(collectionRequest);
        String collectionId =null;
        CollectionResponse collectionResponse = new CollectionResponse();
        try {
            String response = post(url, request);
            collectionResponse = objectMapper.readValue(response, CollectionResponse.class);
            collectionId = collectionResponse.getCollectionId();
            submitCollection(collectionId);
            System.out.println(""+submitCollection(collectionId));
        } catch (Exception e) {
            System.out.printf("Unable to create COLLECTION");
        }
        return collectionResponse;
    }

    public boolean updateCollection(CollectionRequest collectionRequest) throws IOException
    {
        boolean updated =false;
        if(collectionRequest.getCollection_id() == null)
        {
            return false;
        }
        String url = HOST + UPDATE_API + collectionRequest.getCollection_id();
        String request = objectMapper.writeValueAsString(collectionRequest);
        try {
            updated = put(url, request);
            submitCollection(collectionRequest.getCollection_id());
            System.out.println(""+submitCollection(collectionRequest.getCollection_id()));
        } catch (Exception e) {
            System.out.printf("Unable to update COLLECTION "+ updated);
        }
        return updated;
    }


    private boolean submitCollection(String collectionId) {
        boolean submitted = false;
        if(!StringUtils.isNullOrEmpty(collectionId)) {
            String url = HOST + SUBMIT_API + collectionId;
            String request = "{}";
            try {
                submitted = put(url, request);
            } catch (Exception e) {
                System.out.printf("Unable to submit COLLECTION with id " + collectionId);
            }
        }
        return submitted;
    }

    private String post(String url, String json) throws IOException {
        final MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("X_TEAM_ID", "LBHAV2UYG8")
                .addHeader("X_USER_ID", "pushkar.anand@flipkart.com")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean put(String url, String jsonBody) {

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonBody);
        Request request = new Request.Builder()
                .url(url)
                .put(body) //PUT
                .addHeader("X_TEAM_ID", "LBHAV2UYG8")
                .addHeader("X_USER_ID", "pushkar.anand@flipkart.com")
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println("code"+ response.code() );
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
