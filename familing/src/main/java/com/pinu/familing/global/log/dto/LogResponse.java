package com.pinu.familing.global.log.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class LogResponse {
    private List<String> logs;
    private int totalLines;
    private int currentPage;
    private int pageSize;

    public boolean hasNextPage() {
        return currentPage * pageSize < totalLines;
    }
}
