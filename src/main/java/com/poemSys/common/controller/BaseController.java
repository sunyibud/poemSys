package com.poemSys.common.controller;

import com.poemSys.common.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * 基本控制父类
 */
public class BaseController
{
    @Autowired
    HttpServletRequest request;

    @Autowired
    RedisUtil redisUtil;
}
