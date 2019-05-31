package com.moksha.raspberrypi.server.fkService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moksha.raspberrypi.server.ajay.models.entities.UserAction;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GCPConnectService {
    String METHOD_GET = "GET";
    String HOST = "http://35.231.5.148:20000";
    String GET_POLL_API = "v0/gapp/getPendingTasks";
    String GET_UPDATE_API = "v0/gapp/updateTask?action_id={action_id}&talk_back_text={talk_back_text}";

    OkHttpClient client;
    ObjectMapper objectMapper;

    public List<UserAction> getAllPendingUserActions() throws IOException {
        String url = HOST + GET_POLL_API ;
        Request request = new Request.Builder()
                .url(url)
                .build();
        String responseBody = null;
        try (Response response = client.newCall(request).execute()) {
            responseBody = response.body().string();
        }

        objectMapper = new ObjectMapper();

        final List<UserAction> userActions = objectMapper.readValue(responseBody, new TypeReference<List<UserAction>>() {
        });
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
