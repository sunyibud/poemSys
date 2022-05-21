package com.poemSys.user.bean;

import com.poemSys.common.entity.basic.SysPoem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysPoemRes implements Serializable
{
    private static final long serialVersionUID = 1L;

    private long id;
    private String name;
    private String content;
    private String dynasty;
    private String poet;
    private String translation;
    private String notes;
    private String appreciation;
    private String analyse;
    private String background;
    private int likeNum;
    private int collectNum;

    private boolean isLike;
    private boolean isCollect;
}
