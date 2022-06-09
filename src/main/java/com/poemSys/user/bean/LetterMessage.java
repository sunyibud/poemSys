package com.poemSys.user.bean;

import com.poemSys.common.entity.basic.SysLetter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 私信消息列表返回格式
 * 即和每个用户的私信列表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LetterMessage implements Serializable
{
    private static final long serialVersionUID = 1L;

    private long messageId;   //对应的sysMessage的id,方便删除
    private UserInfo toUserId;
    private int newNum; //未读消息的数量
    private List<SysLetterRes> letterList;//消息内容
}
