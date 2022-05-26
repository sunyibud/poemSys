package com.poemSys.user.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用于返回前端的用户详细个人信息（不包含隐私信息）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    private long id;
    private String username;
    private String signature;
    private String sex;
    private String email;
    private String telephone;
    private String headPath;
    private long followNum;
    private long fansNum;
    private long postNum;
    private boolean isFollow;
}
