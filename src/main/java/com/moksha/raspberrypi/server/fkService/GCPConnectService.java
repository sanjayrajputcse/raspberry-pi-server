package com.moksha.raspberrypi.server.fkService;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moksha.raspberrypi.server.ajay.models.entities.UserAction;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GCPConnectService {
    String METHOD_GET = "GET";
    String HOST = "http://35.231.5.148:20000";
    String GET_POLL_API = "/v0/gapp/getPendingTasks";
    String GET_UPDATE_API = "/v0/gapp/updateTask?action_id={action_id}&talk_back_text={talk_back_text}";

    OkHttpClient client;
    ObjectMapper objectMapper;

    public GCPConnectService() {
        client = new OkHttpClient();
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public List<UserAction> getAllPendingUserActions() throws IOException {
        List<UserAction> userActions = Collections.EMPTY_LIST;
        String url = HOST + GET_POLL_API ;
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if(response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                userActions = objectMapper.readValue(responseBody, new TypeReference<List<UserAction>>() {
                });
            }else {
                System.out.println("Empty Response for getAllPendingUserActions()");
                return userActions;
            }
        }catch (Exception e){
            System.out.println(e.getClass());
        }
        return userActions;
    }

    public void setStatusAndDesc(long action_id, String talkBackText) throws IOException {
        String url = HOST + GET_UPDATE_API ;
        StringUtils.replace(url,"{action_id}",String.valueOf(action_id));
        StringUtils.replace(url,"{talk_back_text}",String.valueOf(talkBackText));
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).execute();
    }
}
