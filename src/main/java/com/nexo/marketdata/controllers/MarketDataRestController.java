package com.nexo.marketdata.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexo.marketdata.exceptions.KrakenApiException;
import com.nexo.marketdata.model.rest.OrderBookDTO;
import com.nexo.marketdata.model.enums.KrakenRestApiMethod;
import com.nexo.marketdata.services.rest.KrakenApiService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Controller
public class MarketDataRestController {

    private final KrakenApiService krakenApiService;
    private final ObjectMapper objectMapper;

    public MarketDataRestController(KrakenApiService krakenApiService, ObjectMapper objectMapper) {
        this.krakenApiService = krakenApiService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/data")
    public ResponseEntity<OrderBookDTO> fetchMarketData(
            @RequestParam("pair") String pair
    ) throws IOException {
        Map<String, String> input = new HashMap<>();
        input.put("pair", pair);

        String response = this.krakenApiService.queryPublic(KrakenRestApiMethod.DEPTH, input);

        OrderBookDTO orderBookDTO = this.objectMapper.readValue(response, OrderBookDTO.class);
        System.out.println(orderBookDTO.toString());

        input.clear();

        return ResponseEntity.ok(orderBookDTO);
    }
}
