package com.poemSys.user.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SonComment implements Serializable
{
    private static final long serialVersionUID = 1L;

    private long id;
    private UserInfo ownerUserInfo;
    private UserInfo toUserInfo;
    private String content;
    private LocalDateTime createdTime;
    private boolean isOwner;
}
