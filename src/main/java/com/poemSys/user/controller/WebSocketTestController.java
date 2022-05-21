package com.poemSys.user.controller;

import com.poemSys.common.bean.Result;
import com.poemSys.user.service.general.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * webSocket测试
 */
@RestController
@RequestMapping("/api/ws")
public class WebSocketTestController
{
    @Autowired
    WebSocketService webSocketService;

    @RequestMapping("/onMessage")
    public Result onMessage()
    {
        webSocketService.onMessage("test");
        return new Result();
    }

    @RequestMapping("/sendMessage")
    public Result sendMessage()
    {
        try
        {
            webSocketService.sendMessage("sendMessageTest");
            return new Result();
        }catch (IOException e)
        {
            e.printStackTrace();
            return new Result();
        }
    }
}
