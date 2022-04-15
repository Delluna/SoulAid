package com.example.soulaid.WebSocket;

import com.example.soulaid.dao.ChatMessageDao;
import com.example.soulaid.entity.ChatMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collections;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

/**
 * A simple WebSocketServer implementation. Keeps track of a "chatroom".
 */
public class ChatServer extends WebSocketServer {

    public ChatServer(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }

    public ChatServer(InetSocketAddress address) {
        super(address);
    }

    public ChatServer(int port, Draft_6455 draft) {
        super(new InetSocketAddress(port), Collections.<Draft>singletonList(draft));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conn.send("Welcome to the server!"); //This method sends a message to the new client
        broadcast("new connection: " + handshake
                .getResourceDescriptor()); //This method sends a message to all clients connected
        System.out.println(
                conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        broadcast(conn + " has left the room!");
        System.out.println(conn + " has left the room!");
    }

    //服务器收到消息执行onMessage()
    @Override
    public void onMessage(WebSocket conn, final String message) {

        //先把数据存入数据库
        System.out.println(conn + ": " + message);
        if (!message.equals("Hello, it is me. Mario :)")) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    String copy=message;
                    String msg = copy.split(";")[2].split(":")[1];
                    int type = Integer.valueOf(copy.split(";")[3].split(":")[1]);
                    String tname, uname;
                    if (type == 1) {
                        tname = copy.split(";")[1].split(":")[1];
                        uname = copy.split(";")[0].split(":")[1];
                    } else {
                        tname = copy.split(";")[0].split(":")[1];
                        uname = copy.split(";")[1].split(":")[1];
                    }

                    ChatMessage newMsg = new ChatMessage(msg, tname, uname, type);
                    ChatMessageDao chatMessageDao = new ChatMessageDao();
                    chatMessageDao.addChat(newMsg);

                }
            }).start();
        }
        //将消息广播出去
        broadcast(message);
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        broadcast(message.array());
        System.out.println(conn + ": " + message);
    }


    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        if (conn != null) {
            // some errors like port binding failed may not be assignable to a specific websocket
        }
    }

    @Override
    public void onStart() {
        System.out.println("Server started!");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
    }


    public static void main(String[] args) throws InterruptedException, IOException {
        int port = 8887; // 843 flash policy port
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception ex) {
        }
        ChatServer s = new ChatServer(port);
        s.start();
        System.out.println("ChatServer started on port: " + s.getPort());

        BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String in = sysin.readLine();
            s.broadcast(in);
            if (in.equals("exit")) {
                s.stop(1000);
                break;
            }
        }
    }

}
