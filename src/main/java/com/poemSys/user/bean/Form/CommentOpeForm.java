package com.poemSys.user.bean.Form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentOpeForm implements Serializable
{
    private static final long serialVersionUID = 1L;

    private int type;   //一级评论type=1, 二级评论type=2
    private long id; //为一级评论时表示postId, 二级评论时表示commentId;
    private String content;
}
