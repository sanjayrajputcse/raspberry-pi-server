package com.moksha.raspberrypi.server.fkService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.moksha.raspberrypi.server.models.PNRequest;
import com.mysql.jdbc.StringUtils;
import okhttp3.*;

import java.io.*;

/**
 * Created by somil.jain on 31/05/19.
 */
public class PNService {
    String HOST = "http://10.47.0.120";
    String API = "/v2/send/push/unknown/retailapp";
    OkHttpClient client ;

    public PNService() {
        client = new OkHttpClient();
    }

    public boolean sendPushNotification(PNRequest pnRequest) throws IOException {
        try {
            File file = new File(getClass().getClassLoader().getResource("local/PNRequestData.json").getFile());
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(file);
            updateId(jsonNode);
            JsonNode channelInfo = jsonNode.get("channelInfo");
            updateDeviceId(pnRequest, (ObjectNode) channelInfo);
            JsonNode action = jsonNode.get("channelData").get("data").get("action");
            updateURLs(action,pnRequest);
            if(!StringUtils.isNullOrEmpty(pnRequest.getPnTitle()))
            {
                JsonNode dataJsonNode = jsonNode.get("channelData").get("data");
                updateText(dataJsonNode,pnRequest.getPnTitle());
            }
            String URL = HOST + API;
            System.out.println("request: "+objectMapper.writeValueAsString(jsonNode));
            return post(URL, objectMapper.writeValueAsString(jsonNode));
        } catch (Exception e) {
            System.out.println("Couldn't send the PN: "+e);
        }
        return false;
    }

    private void updateText(JsonNode dataJsonNode, String pnTitle) {
        ((ObjectNode)dataJsonNode).put("title",pnTitle);
    }

    private void updateURLs(JsonNode actionNode, PNRequest pnRequest) {
        String collectionUrl = pnRequest.getCollectionUrl();
        if(StringUtils.isNullOrEmpty(collectionUrl))
        {
            return;
        }
        else {
            ((ObjectNode) actionNode).put("originalUrl",collectionUrl);
            ((ObjectNode) actionNode).put("url",collectionUrl);
            JsonNode params = actionNode.get("params");
            ((ObjectNode) params).put("url",collectionUrl);
        }
    }

    private void updateId(JsonNode jsonNode) {
        JsonNode dataJsonNode = jsonNode.get("channelData").get("data");
        ((ObjectNode) dataJsonNode).put("id",System.currentTimeMillis());
    }

    private void updateDeviceId(PNRequest pnRequest, ObjectNode channelInfo) {
        pnRequest.getDeviceIds().forEach(deviceId -> {
            ((ArrayNode)channelInfo.putArray("deviceIds")).add(deviceId);
        });
    }

    private boolean post(String url, String jsonBody) {

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonBody);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("x-api-key","bw3VtGZYhTC4C8NDa94ybX9hf5wXqkuMgZsSRkNUdvNegQC8")
                .addHeader("x-perf-test", "false")
                .addHeader("x-request-id",String.valueOf(System.currentTimeMillis()))
                .post(body)
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
