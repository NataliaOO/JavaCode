package com.example.api;

import com.example.api.dto.EventResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class EventController {
    @GetMapping("/event")
    public EventResponse getEvent() {
        return new EventResponse(LocalDateTime.now());
    }
}
