package com.poemSys.user.service.general;


import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.poemSys.user.bean.WebsocketMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
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
    private static final ConcurrentHashMap<Long, WebSocketService> webSocketMap = new ConcurrentHashMap<>();
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
            //删除之前保存的连接，从新放入新的
            webSocketMap.remove(userId);
            webSocketMap.put(userId, this);
        } else
        {
            webSocketMap.put(userId, this);
            addOnlineCount();
        }
        log.info("用户" + userId + "(id)连接成功,当前在线人数为:" + getOnlineCount());

        sendMessage(new WebsocketMsg(1, 0, userId, "连接成功"));
    }

    @OnClose
    public void onClose()
    {
        if (webSocketMap.containsKey(userId))
        {
            webSocketMap.remove(userId);
            subOnlineCount();
        }
        log.info("用户" + userId + "(id)退出,当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端发送来的消息调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public boolean onMessage(String message)
    {
        log.info("用户" + userId + "(id)向服务端发送消息,内容:" + message);
        //消息保存到数据库、redis
        if (StringUtils.isNotBlank(message))
        {
            //解析发送的报文
            JSONObject jsonObject = JSON.parseObject(message);
            Long toUserId = jsonObject.getLong("toUserId");
            String content = jsonObject.getString("content");

            //传送给对应toUserId用户的websocket
            if (webSocketMap.containsKey(toUserId))
            {
                WebsocketMsg websocketMsg = new WebsocketMsg(2, this.userId
                        , toUserId, content);
                webSocketMap.get(toUserId).sendMessage(websocketMsg);
                log.info("用户" + userId + "向用户" + toUserId + "ws消息发送成功");
            }
            log.info("用户" + userId + "向" + toUserId + "发送ws消息,其不在线");
            //否则不在这个服务器上，发送到mysql或者redis
            return true;
        }
        log.info("onMessage接受到的消息json字符内容为空");
        return false;
    }

    //通过客户端调用发送消息
    public void sendMessage(WebsocketMsg message)
    {
        this.session.getAsyncRemote().sendText(JSONUtil.toJsonStr(message));
    }

    //在服务端调用发送消息
    public void sendMessageOnServer(Long toUserId, WebsocketMsg message)
    {
        message.setToUserId(toUserId);
        if(webSocketMap.containsKey(toUserId))
        {
            webSocketMap.get(toUserId).sendMessage(message);
        }
    }

//    /**
//     * 群发消息
//     *
//     * @param message 消息内容
//     * @throws IOException io异常
//     */
//    public static void sendAllMessage(String message) throws IOException
//    {
//        log.info("群发消息:" + "内容:" + message);
//        webSocketMap.forEach((k, v) ->
//        {
//            try
//            {
//                v.sendMessage(message);
//            } catch (IOException e)
//            {
//                log.error("群发消息异常");
//            }
//        });
//    }

    public static synchronized int getOnlineCount()
    {
        return WebSocketService.onlineCount;
    }

    public static synchronized void addOnlineCount()
    {
        WebSocketService.onlineCount++;
    }

    public static synchronized void subOnlineCount()
    {
        WebSocketService.onlineCount--;
    }
}
