package com.poemSys.user.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysPostRes implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private long id;
    private String title;
    private String content;
    private LocalDateTime createdTime;
    private int collectNum;
    private int likeNum;
    private String coverImage;

    private boolean isLike;
    private boolean isCollect;
}
