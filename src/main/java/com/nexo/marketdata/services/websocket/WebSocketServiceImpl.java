package com.nexo.marketdata.services.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.nexo.marketdata.services.websocket.handlers.KrakenHandler;
import lombok.val;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.Random;

@Service
public class WebSocketServiceImpl extends TextWebSocketHandler implements WebSocketService {

    private final KrakenHandler publicClient;

    /**
     * Constructor
     */
    public WebSocketServiceImpl() {
        this.publicClient = new KrakenHandler();
    }

    /**
     * Builds a json object from the four request params given by the user
     * It is then sent as a string to the server.
     * It is sent through a client session message.
     *
     * @param pairs
     * @param interval
     * @param depth
     * @param name
     */
    @Override
    public void subscribe(List<String> pairs, int interval, int depth, String name) {
        var json = new ObjectMapper().createObjectNode();
        json.put("event", "subscribe");

        val reqId = Math.abs(new Random().nextInt());
        json.put("reqid", reqId);

        if (pairs != null) {
            json.putArray("pair").addAll((ArrayNode) new ObjectMapper().valueToTree(pairs));
        }

        val subscription = json.putObject("subscription");
        subscription.put("name", name);

        this.publicClient.sendAndConfirm(json.toString(), reqId);
    }

    /**
     * Validates if the client is connected.
     *
     * If not, then connect.
     */
    @Override
    public boolean connect() {
        val publicCon = this.publicClient.connected()
                ? this.publicClient.connected()
                : this.publicClient.connect();

        return publicCon;
    }

    /**
     * Closes the connection.
     */
    @Override
    public void close() {
        this.publicClient.close();
    }
}
