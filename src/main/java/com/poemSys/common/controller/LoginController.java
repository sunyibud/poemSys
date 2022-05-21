package com.poemSys.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 登录控制器
 */
@Controller
public class LoginController
{
    @GetMapping("/login")
    public String login()
    {
        return "login";
    }
}
