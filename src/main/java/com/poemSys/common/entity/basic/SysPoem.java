package com.poemSys.common.entity.basic;

import com.baomidou.mybatisplus.annotation.TableName;
import com.poemSys.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_poem")
public class SysPoem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

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
}
