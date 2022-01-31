package com.nexo.marketdata.controllers;

import com.nexo.marketdata.services.websocket.WebSocketService;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MarketDataWebsocketController {

    private final WebSocketService webSocketService;

    public MarketDataWebsocketController(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @PostMapping("/connect")
    public ResponseEntity<String> connect() {

        val success = this.webSocketService.connect();

        if (success) {
            return ResponseEntity.ok("{\"msg\": \"connected to Kraken\"}");
        } else {
            return ResponseEntity.ok("{\"msg\": \"error connecting to Kraken\"}");
        }
    }

    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribe(
            @RequestParam(value = "pair", required = false) List<String> pairs,
            @RequestParam(value = "interval", defaultValue = "5") int interval,
            @RequestParam(value = "depth", defaultValue = "10") int depth,
            @RequestParam("name") String name
    ) {
        try {
            this.webSocketService.subscribe(pairs, interval, depth, name);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("{error:\"" + e.getMessage() + "\"}");
        }
        return ResponseEntity.ok("{\"msg\": \"subscribed\"}");
    }

    @PostMapping ("/close")
    public ResponseEntity<String> close() {
        this.webSocketService.close();
        return ResponseEntity.ok("{}");
    }
}
