package com.nexo.marketdata.services.websocket.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexo.marketdata.constants.KrakenUrlConstants;
import com.nexo.marketdata.model.websoket.OrderBookWebsocketDTO;
import com.nexo.marketdata.model.websoket.SubscriptionDTO;
import lombok.Getter;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class KrakenHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(KrakenHandler.class);
    private static final ObjectMapper jsonMapper = new ObjectMapper();

    private WebSocketSession clientSession;
    private final ConcurrentHashMap<Integer, String> errorMessages;

    public KrakenHandler() {
        this.errorMessages = new ConcurrentHashMap<>(500);
    }

    public void sendMessage(String msg) {
        try {
            this.clientSession.sendMessage(new TextMessage(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendAndConfirm(String msg, int reqId) {
        String errorMsg = null;
        try {
            System.out.println(new TextMessage(msg).getPayload());
            this.clientSession.sendMessage(new TextMessage(msg));
            int i = 1;
            do {
                errorMsg = this.errorMessages.get(reqId);
                Thread.sleep(i * 1000L);
            }
            while (i++ < 4 && errorMsg == null);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }
        if (errorMsg == null) {
            throw new RuntimeException(String.format("Timeout while waiting for reqId=%s response", reqId));
        } else {
            if (!errorMsg.isEmpty()) {
                throw new RuntimeException(errorMsg);
            }
        }
    }

    @Override
    public void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) {

        try {
            Object jsonResponseObject = jsonMapper.readValue(message.getPayload(), Object.class);
            System.out.println(jsonResponseObject);
            if (jsonResponseObject instanceof Map) {
                String event = (String) ((Map) jsonResponseObject).get("event");

                switch (event) {
                    case "ping": case "pong": case "heartbeat": case "systemStatus": break;
                    case "subscriptionStatus":
                        Object status = ((Map) jsonResponseObject).get("status");
                        if (status.equals("error")) {
                            LOGGER.debug("Error: " + ((Map) (jsonResponseObject)).get("errorMessage"));
                        }
                        else {
                            SubscriptionDTO subscriptionDTO = jsonMapper.convertValue(jsonResponseObject, SubscriptionDTO.class);
                            System.out.println(subscriptionDTO);
                        }
                        break;
                    default:
                        LOGGER.debug("Unknown event type: {}", event);
                }
            }

            if (jsonResponseObject instanceof List) {
                List jsonList = (List) jsonResponseObject;

                OrderBookWebsocketDTO orderBookDTO = jsonMapper.convertValue(jsonList.get(1), OrderBookWebsocketDTO.class);
                System.out.println(orderBookDTO.toString());
                System.out.println(jsonList.get(3) + "\n\n\n"); // Pair of Currencies
            }

        } catch(JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public boolean connect() {
        try {
            var webSocketClient = new StandardWebSocketClient();
            this.clientSession = webSocketClient.doHandshake(
                    this,
                    new WebSocketHttpHeaders(),
                    URI.create(KrakenUrlConstants.WEBSOCKET_PUBLIC_URL)).get();

            System.out.println("Successfully connected to Kraken websocket");
            return true;
        } catch (Exception e) {
            LOGGER.error("Exception while creating websockets", e);
        }
        return false;
    }

    public void close() {
        try {
            this.clientSession.close();
            LOGGER.info("Session closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        LOGGER.info("Kraken: Connection Closed!");
    }

    public boolean connected() {
        return this.clientSession != null && this.clientSession.isOpen();
    }
}
