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
@TableName("sys_comment")
public class SysComment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private long ownerUserId;
    private String content;
    private LocalDateTime createdTime;
    private int type;
    private long postOrFather;
    private String uuid;
}
