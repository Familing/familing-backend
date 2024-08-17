package com.pinu.familing.domain.userstatus.dto;

import com.pinu.familing.domain.userstatus.entity.Userstatus;

public record UserstatusResponse(Long id,
                                 String text) {

    public UserstatusResponse(Userstatus userstatus) {
        this(userstatus.getId(),userstatus.getText());
    }
}
