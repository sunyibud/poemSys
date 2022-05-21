package com.poemSys.common.entity.basic;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.poemSys.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("sys_user")
public class SysUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String signature;
    private String sex;
    private String email;
    private String telephone;
    private String headPath;
    private boolean identify;
    private LocalDateTime registerTime;
    private boolean state;//账号状态

    @TableField(updateStrategy = FieldStrategy.IGNORED)//为空时也可以设置
    private LocalDateTime unlockTime;//解封时间
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String lockReason;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String lockAdmin;

    private long followNum;
    private long fansNum;
}
