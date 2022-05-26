package com.poemSys.user.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 前台系统用户信息分页结果封装
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoPageAns
{
    private static final long serialVersionUID = 1L;
    private Long total;
    private Long size;
    private Long current;
    private Long pages;
    private List<UserInfo> records;
}
