package com.poemSys.user.service.general;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ServerEndpoint("/ws/{userId}")
@Component
public class WebSocketService
{
    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static ConcurrentHashMap<Long, WebSocketService> webSocketMap = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * 当前连接用户userId
     */
    private Long userId;

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Long userId)
    {
        this.session = session;
        this.userId = userId;
        //当用户已经建立过连接时
        if (webSocketMap.containsKey(userId))
        {
            webSocketMap.remove(userId);
            webSocketMap.put(userId, this);
        } else
        {
            webSocketMap.put(userId, this);
            addOnlineCount();
        }
        log.info("用户" + userId + "(id)连接成功,当前在线人数为:"+getOnlineCount());

        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            log.error("用户:"+userId+"(id),网络异常!");
        }
    }

    @OnClose
    public void onClose() {
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            subOnlineCount();
        }
        log.info("用户"+userId+"(id)退出,当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端发送来的消息调用的方法
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("用户"+userId+"(id)向服务端发送消息,内容:"+message);
        //可以群发消息
        //消息保存到数据库、redis
        if(StringUtils.isNotBlank(message)){
            try {
                //解析发送的报文
                JSONObject jsonObject = JSON.parseObject(message);
                //追加发送人(防止串改)
                jsonObject.put("fromUserId",this.userId);
                Long toUserId=jsonObject.getLong("toUserId");
                //传送给对应toUserId用户的websocket
                if(webSocketMap.containsKey(toUserId)){
                    webSocketMap.get(toUserId).sendMessage(jsonObject.toJSONString());
                }else{
                    log.error("请求的userId:"+toUserId+"不在该服务器上");
                    //否则不在这个服务器上，发送到mysql或者redis
                }
            }catch (Exception e){
                log.error("onMessage异常");
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) throws IOException {
        this.session.getAsyncRemote().sendText(message);
    }

    /**
     * 群发消息
     * @param message 消息内容
     * @throws IOException
     */
    public static void sendAllMessage(String message) throws IOException {
        log.info("群发消息:"+"内容:"+message);
        webSocketMap.forEach((k, v)->{
            try
            {
                v.sendMessage(message);
            }catch (IOException e)
            {
                log.error("群发消息异常");
            }
        });
    }

    public static synchronized int getOnlineCount() {
        return WebSocketService.onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketService.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketService.onlineCount--;
    }
}
