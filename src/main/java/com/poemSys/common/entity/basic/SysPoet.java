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
@TableName("sys_poet")
public class SysPoet extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private String name;
    private String dynasty;
    private String introduce;
    private String headIcon;
}
