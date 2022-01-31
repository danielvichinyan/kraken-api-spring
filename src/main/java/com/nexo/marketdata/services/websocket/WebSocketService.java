package com.nexo.marketdata.services.websocket;

import java.util.List;

public interface WebSocketService {

    boolean connect();

    void subscribe(List<String> pairs, int interval, int depth, String name);

    void unsubscribe(List<String> channelIds, String name);

    void close();
}
