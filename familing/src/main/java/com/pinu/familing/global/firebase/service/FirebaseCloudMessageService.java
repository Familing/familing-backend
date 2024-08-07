package com.pinu.familing.global.firebase.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.pinu.familing.global.firebase.dto.FcmMessage;
import com.squareup.okhttp.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;


@Component
@RequiredArgsConstructor
public class FirebaseCloudMessageService {


    private final String API_URL = "https://fcm.googleapis.com/v1/projects/familing-def7e/messages:send";
    private final String YOUR_SERVER_KEY = "42540005161";
    private static final String SCOPES = "https://www.googleapis.com/auth/firebase.messaging";

    private final ObjectMapper objectMapper;

    public void sendMessageTo(String targetToken, String title, String body) throws IOException, JsonProcessingException {

        String message = makeMessage(targetToken, title, body);
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON, message);

        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + getAccessToken())
                .addHeader("Content-Type", "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();

        System.out.println("Response Code: " + response.code());
        System.out.println("Response Body: " + response.body().string());

    }

    private String makeMessage(String targetToken, String title, String body) throws JsonParseException, JsonProcessingException {
        FcmMessage fcmMessage = FcmMessage.builder()
                .message(FcmMessage.Message.builder()
                        .token(targetToken)
                        .notification(FcmMessage.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();

        return objectMapper.writeValueAsString(fcmMessage);
    }

    private static String getAccessToken() throws IOException {
//        GoogleCredentials googleCredentials = GoogleCredentials
//                .fromStream(new ClassPathResource("firebase/firebase_service_key.json").getInputStream())
//                .createScoped(Arrays.asList(SCOPES));
//        googleCredentials.refreshAccessToken();
//        return googleCredentials.getAccessToken().getTokenValue();
        try {
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource("firebase/firebase_service_key.json").getInputStream())
                    .createScoped(Arrays.asList(SCOPES));

            googleCredentials.refreshIfExpired();
            if (googleCredentials.getAccessToken() == null) {
                googleCredentials.refresh();

            }
            System.out.println("googleCredentials.getAccessToken().getTokenValue() = " + googleCredentials.getAccessToken().getTokenValue());
            return googleCredentials.getAccessToken().getTokenValue();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to obtain access token", e);
        }
    }

}