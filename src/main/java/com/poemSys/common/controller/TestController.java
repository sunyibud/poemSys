package com.poemSys.common.controller;

import com.poemSys.common.bean.Result;
import com.poemSys.common.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController
{
    @Autowired
    SysUserService sysUserService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @PreAuthorize("hasRole('admin')")
    @GetMapping("/test")
    public Result test()
    {
        String password = bCryptPasswordEncoder.encode("123456");

        boolean matches = bCryptPasswordEncoder.matches("123456", password);

        System.out.println("匹配结果， "+matches);
        return new Result(0, "", password);
    }

    @PreAuthorize("hasAnyAuthority('sys:user:list')")
    @GetMapping("/power")
    public Result power()
    {
        return new Result(0, "power veritfy", null);
    }
}
