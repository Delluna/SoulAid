package com.example.soulaid.WebSocket;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

/**
 * This example demonstrates how to create a websocket connection to a server. Only the most
 * important callbacks are overloaded.
 */
public class ChatClient extends WebSocketClient {

    public static String uri="ws://192.168.101.28:8887";
    public static URI serverURI = URI.create(uri);

    public ChatClient(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    public ChatClient(URI serverURI) {
        super(serverURI);
    }

    public ChatClient(URI serverUri, Map<String, String> httpHeaders) {
        super(serverUri, httpHeaders);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        send("Hello, it is me. Mario :)");
        System.out.println("opened connection");
        // if you plan to refuse connection based on ip or httpfields overload: onWebsocketHandshakeReceivedAsClient
    }

    //客户端收到消息执行onMessage()
    @Override
    public void onMessage(String message) {
        System.out.println("received: " + message);

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        // The close codes are documented in class org.java_websocket.framing.CloseFrame
        System.out.println(
                "Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: "
                        + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
        // if the error is fatal then onClose will be called additionally
    }

}
