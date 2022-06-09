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
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_feedback")
public class SysFeedback extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private long applyUserId;
    private String content;
    private boolean state;
    private LocalDateTime createdTime;

    @TableField(updateStrategy = FieldStrategy.IGNORED)//为空时也可以设置
    private LocalDateTime solveTime;

    private String feedbackInfo;
    private long processorUserId;
}
