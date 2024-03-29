package com.example.netty.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.example.netty.vo.Message;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 夏先鹏
 * @date 2019/07/22
 * @time 20:34
 */
@Service
public class MessageEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(MessageEventHandler.class);

    @Autowired
    SocketIOServer server;

    private static Map<String, SocketIOClient> clientMap = new HashMap<>();

    @OnConnect
    public void onConnect(SocketIOClient client) {
        String uuid = client.getSessionId().toString();
        clientMap.put(uuid, client);
        logger.info("ip: " + client.getRemoteAddress().toString() + ",uuid: " + uuid + " connect");
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        String uuid = client.getSessionId().toString();
        clientMap.remove(uuid);
        logger.info("ip: " + client.getRemoteAddress().toString() + ",uuid: " + uuid + " disConnect");
    }

    @OnEvent(value = "message")
    public void message(SocketIOClient client, Message message) {
        /*String username = client.get("username");
        String message = client.get("message");*/
        System.out.println("u: " + message.getUsername() + ", m: " + message.getMessage());
        sendMessageToAllClient("recieve", "get out ...");
    }

    public void sendMessageToAllClient(String eventType, String message) {
        Collection<SocketIOClient> clients = server.getAllClients();
        for (SocketIOClient client : clients) {
            client.sendEvent(eventType, message);
        }
    }


}
