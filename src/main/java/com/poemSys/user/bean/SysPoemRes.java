package com.poemSys.user.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 相较于sysPoem添加了isLike和isCollect
 */
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
    private String QRCodeBase64Img;
}
