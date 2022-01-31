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

    public WebSocketServiceImpl() {
        this.publicClient = new KrakenHandler();
    }

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

    @Override
    public void unsubscribe(List<String> channelIds, String name) {
        var json = new ObjectMapper().createObjectNode();
        json.put("event", "unsubscribe");
        if (!channelIds.isEmpty()) {
            json.putArray("pair").addAll((ArrayNode) new ObjectMapper().valueToTree(channelIds));
        }
        val subscription = json.putObject("subscription");
        subscription.put("name", name);

        this.publicClient.sendMessage(json.toString());
    }

    @Override
    public boolean connect() {
        val publicCon = this.publicClient.connected() ? this.publicClient.connected() : this.publicClient.connect();
        return publicCon;
    }

    @Override
    public void close() {
        this.publicClient.close();
    }
}
