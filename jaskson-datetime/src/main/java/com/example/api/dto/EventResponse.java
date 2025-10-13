package com.example.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class EventResponse {
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy:MM:dd'##':HH:mm:ss:SSS",
            locale = "ru_RU",
            timezone = "Europa/Moscow"
    )
    private final LocalDateTime eventTime;

    public EventResponse(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }
}
