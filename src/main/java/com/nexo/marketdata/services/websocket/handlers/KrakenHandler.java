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

    /**
     * Constructor
     */
    public KrakenHandler() {
        this.errorMessages = new ConcurrentHashMap<>(500);
    }

    /**
     * Sends a message to the server via the client session.
     *
     * @param msg
     * @param reqId
     */
    public void sendAndConfirm(String msg, int reqId) {
        String errorMsg = null;
        try {
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

    /**
     * Handles every message(response) from the API.
     * The logic is applied to each generated message(response) from the websocket stream.
     *
     * @param session
     * @param message
     */
    @Override
    public void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) {

        // Start reading generated responses
        try {
            // Create an object from the message
            Object jsonResponseObject = jsonMapper.readValue(message.getPayload(), Object.class);

            if (jsonResponseObject instanceof Map) {
                // get the event key of the object
                String event = (String) ((Map) jsonResponseObject).get("event");

                switch (event) {
                    // System messages which do not require any operations
                    case "ping": case "pong": case "heartbeat": case "systemStatus": break;

                    // The status of the subscription
                    case "subscriptionStatus":
                        Object status = ((Map) jsonResponseObject).get("status");
                        if (status.equals("error")) {
                            LOGGER.debug("Error: " + ((Map) (jsonResponseObject)).get("errorMessage"));
                        }
                        else {
                            // Convert the message into a SubscriptionDTO
                            SubscriptionDTO subscriptionDTO = jsonMapper.convertValue(jsonResponseObject, SubscriptionDTO.class);
                        }
                        break;
                    default:
                        LOGGER.debug("Unknown event type: {}", event);
                }
            }

            // If the object is an instance of List, we need this message because it contains the order book
            if (jsonResponseObject instanceof List) {
                List jsonList = (List) jsonResponseObject;

                // Convert the Object that comes in the message to an OrderBook
                OrderBookWebsocketDTO orderBookDTO = jsonMapper.convertValue(jsonList.get(1), OrderBookWebsocketDTO.class);
                System.out.println(orderBookDTO.toString());
                System.out.println(jsonList.get(3) + "\n\n\n"); // Pair of Currencies
            }

        } catch(JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Does a handshake between the server (API) and us (Client).
     * Initialises the connection.
     */
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

    /**
     * Closes the client session.
     */
    public void close() {
        try {
            this.clientSession.close();
            LOGGER.info("Session closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Activates when the connection is closed.
     *
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        LOGGER.info("Kraken: Connection Closed!");
    }

    /**
     * Validates if we are connected to the server.
     */
    public boolean connected() {
        return this.clientSession != null && this.clientSession.isOpen();
    }
}
