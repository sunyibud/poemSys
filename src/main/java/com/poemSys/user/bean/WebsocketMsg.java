package com.poemSys.user.bean;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebsocketMsg implements Serializable
{
    private static final long serialVersionUID = 1L;

    private int type;
    private long sendUserId;
    private long toUserId;
    private String content;
}
