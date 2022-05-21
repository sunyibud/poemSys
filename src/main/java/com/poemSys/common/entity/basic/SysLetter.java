package com.poemSys.common.entity.basic;

import com.baomidou.mybatisplus.annotation.TableName;
import com.poemSys.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 私信内容
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_letter")
public class SysLetter extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private long sendUserId;
    private long receiveUserId;
    private String content;
    private LocalDateTime time;
    private boolean state;
    private String uuid;
}
