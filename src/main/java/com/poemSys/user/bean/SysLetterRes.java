package com.poemSys.user.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysLetterRes
{
    private static final long serialVersionUID = 1L;

    private UserInfo sendUserInfo;
    private UserInfo receiveUserInfo;
    private String content;
    private LocalDateTime time;
    private boolean state;//接收方是否已读
}
