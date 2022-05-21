package com.poemSys.user.controller;

import com.poemSys.admin.bean.Form.IdForm;
import com.poemSys.common.bean.Result;
import com.poemSys.user.bean.Form.SendLetterForm;
import com.poemSys.user.service.userOpe.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 除了论坛模块中的所有用户操作操作api
 */
@RestController
@RequestMapping("/api/ope")
public class UserOpeController
{
    @Autowired
    PoemLikeService poemLikeService;

    @Autowired
    PoemCollectService poemCollectService;

    @Autowired
    ReadLetterService readLetterService;

    @Autowired
    SendLetterService sendLetterService;

    @Autowired
    DeleteMessageService deleteMessageService;

    @Autowired
    FollowUserService followUserService;

    /**
     * 点赞和取消点赞古诗词
     * @param idForm 古诗词id
     */
    @PostMapping("/poemLike")
    public Result poemLike(@RequestBody IdForm idForm)
    {
        return poemLikeService.ope(idForm);
    }

    /**
     * 收藏和取消收藏
     */
    @PostMapping("/poemCollect")
    public Result poemCollect(@RequestBody IdForm idForm)
    {
        return poemCollectService.ope(idForm);
    }

    /**
     * 用户点击某条私信（与某个用户的）,设该条私信的所有未读(sysLetter)为已读, 并
     * 设消息(sysMessage)为已读
     * @param idForm 点击的私信id(sysMessage)
     */
    @PostMapping("/readLetter")
    public Result readLetter(@RequestBody IdForm idForm)
    {
        return readLetterService.read(idForm);
    }

    /**
     * 发送私信
     * @param sendLetterForm 接收用户id和私信内容
     */
    @PostMapping("/sendLetter")
    public Result sendLetter(@RequestBody SendLetterForm sendLetterForm)
    {
        return sendLetterService.send(sendLetterForm);
    }

    /**
     * 在消息列表中删除消息（普通消息和私信消息）
     * @param idForm sysMessage.id
     */
    @PostMapping("/deleteMessage")
    public Result deleteMessage(@RequestBody IdForm idForm)
    {
        return deleteMessageService.delete(idForm);
    }

    @PostMapping("/followUser")
    public Result follow(@RequestBody IdForm idForm)
    {
        return followUserService.follow(idForm);
    }
}
