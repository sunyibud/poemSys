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
    private long ownerUserId;
    private long toUserId;
    private String content;
    private LocalDateTime createdTime;
}
