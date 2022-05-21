package com.poemSys.common.entity.basic;

import com.baomidou.mybatisplus.annotation.TableName;
import com.poemSys.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_post")
public class SysPost extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private String title;
    private String content;
    private LocalDateTime createdTime;
    private int collectNum;
    private int likeNum;
}
