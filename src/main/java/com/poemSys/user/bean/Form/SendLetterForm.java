package com.poemSys.user.bean.Form;

import lombok.Data;

import java.io.Serializable;

@Data
public class SendLetterForm implements Serializable
{
    private static final long serialVersionUID = 1L;

    private long toUserId;
    private String content;
}
