package com.poemSys.user.bean.Form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCommentForm implements Serializable
{
    private static final long serialVersionUID = 1L;

    private int type;   //一级评论type=1, 二级评论type=2
    private long postId;
    private long commentId; // 二级评论表示回复的评论id
    private String content;
}
