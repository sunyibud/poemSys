package com.poemSys.user.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageNum implements Serializable
{
    private static final long serialVersionUID = 1L;

    private int total;
    private int comment;
    private int newFans;
    private int likeOrCollect;
    private int sysMessage;
    private int letter;
}
