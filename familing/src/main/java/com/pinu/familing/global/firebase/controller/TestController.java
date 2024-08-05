package com.pinu.familing.global.firebase.controller;

import com.pinu.familing.global.firebase.dto.RequestDTO;
import com.pinu.familing.global.firebase.service.FirebaseCloudMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final FirebaseCloudMessageService firebaseCloudMessageService;

    @PostMapping("/api/fcm")
    public ResponseEntity<?> pushMessage(@RequestBody RequestDTO requestDTO) throws IOException, IOException {
        System.out.println(requestDTO.targetToken() + " "
                + requestDTO.title() + " " + requestDTO.body());

        firebaseCloudMessageService.sendMessageTo(
                requestDTO.targetToken(),
                requestDTO.title(),
                requestDTO.body());
        return ResponseEntity.ok().build();
    }
}
