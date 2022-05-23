package com.poemSys.user.controller;


import com.poemSys.common.bean.Result;
import com.poemSys.user.bean.Form.*;
import com.poemSys.user.bean.UserInfo;
import com.poemSys.user.service.general.*;
import com.poemSys.user.service.userInfo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户个人信息模块接口
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserInfoController
{

    @Autowired
    GetUserInfoByJwtService getUserInfoByJwtService;

    @Autowired
    UpdateLoginUserInfoService updateLoginUserInfoService;

    @Autowired
    ImageUploadService imageUploadService;

    @Autowired
    ChangePasswordService changePasswordService;

    @Autowired
    ResetPasswordService resetPasswordService;

    @Autowired
    RegisterService registerService;

    @Autowired
    SendEmailService sendEmailService;

    @Autowired
    FindPasswordService findPasswordService;

    @Autowired
    MyFollowService myFollowService;

    @Autowired
    MyFansService myFansService;

    @Autowired
    MyPoemCollectService myPoemCollectService;

    @Autowired
    MessageNumService messageNumService;

    @Autowired
    MyMessageService myMessageService;

    @Autowired
    MyPostCollectService myPostCollectService;

    @RequestMapping("/sendEmailToLoginUser")
    public Result sendEmailToLoginUser()
    {
        String emailAddress = getUserInfoByJwtService.getUserInfo().getEmail();
        return sendEmailService.send(emailAddress);
    }

    @RequestMapping("/sendEmailToAddress")
    public Result sendEmailToAddress(@RequestBody EmailAddressForm emailAddressForm)
    {
        return sendEmailService.send(emailAddressForm.getEmailAddress());
    }

    @PostMapping("/register")
    public Result userRegister(@RequestBody UserRegisterForm userRegisterForm)
    {
        return registerService.register(userRegisterForm);
    }

    @PostMapping("/loginUserInfo")
    public Result getLoginUserInfo()
    {
        UserInfo userInfo = getUserInfoByJwtService.getUserInfo();
        return new Result(0, "获取登录用户信息成功", userInfo);
    }

    @PostMapping("/updateUserInfo")
    public Result updateUserInfo(@RequestBody UpdateUserInfoForm updateUserInfoForm)
    {
        return updateLoginUserInfoService.update(updateUserInfoForm);
    }

    @PostMapping("/uploadHeadIcon")
    public Result uploadHeadIcon(MultipartFile file)
    {
        return imageUploadService.upload(file, "/images/headIcons/");
    }


    @PostMapping("/changePassword")
    public Result changePassword(@RequestBody ChangePasswordForm passwordForm)
    {
        return changePasswordService.change(passwordForm);
    }

    @PostMapping("/resetPassword")
    public Result resetPassword(@RequestBody ResetPasswordForm resetPasswordForm)
    {
        return resetPasswordService.reset(resetPasswordForm);
    }

    /**
     * 未登录时重置/找回密码
     */
    @PostMapping("/findPassword")
    public Result findPassword(@RequestBody FindPasswordForm findPasswordForm)
    {
        return findPasswordService.find(findPasswordForm);
    }

    /**
     * 分页获取我的关注列表
     */
    @GetMapping("/myFollow/{page}/{size}")
    public Result myFollow(@PathVariable("page") Integer page,
                           @PathVariable("size") Integer size)
    {
        return myFollowService.getPageList(page, size);
    }

    /**
     * 分页获取我的粉丝列表
     */
    @GetMapping("/myFans/{page}/{size}")
    public Result myFans(@PathVariable("page") Integer page,
                         @PathVariable("size") Integer size)
    {
        return myFansService.getPageList(page, size);
    }

    /**
     * 分页获取我的古诗收藏列表
     */
    @GetMapping("/myPoemCollect/{page}/{size}")
    public Result myPoemCollect(@PathVariable("page") Integer page,
                                @PathVariable("size") Integer size)
    {
        return myPoemCollectService.getPageList(page, size);
    }

    /**
     * 分页获取我的帖子收藏列表
     */
    @GetMapping("/myPostCollect/{page}/{size}")
    public Result myPostCollect(@PathVariable("page") Integer page,
                                @PathVariable("size") Integer size)
    {
        return myPostCollectService.getPageList(page, size);
    }

    /**
     * 获取我的各种类型未读消息的数量
     */
    @PostMapping("/newMessageNum")
    public Result newMessageNum()
    {
        return messageNumService.get();
    }

    /**
     * 获取我的各个种类的消息的内容，访问接口表示该种类消息状态改为已读（除私信）
     * @param type 消息种类（1：comment 2：newFans 3：likeOrCollect 4：sysMessage 5：letter）
     */
    @PostMapping("/myMessage/{type}")
    public Result myMessage(@PathVariable("type") Integer type)
    {
        return myMessageService.get(type);
    }
}
