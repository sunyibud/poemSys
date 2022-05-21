package com.poemSys.common.entity.connection;

import com.baomidou.mybatisplus.annotation.TableName;
import com.poemSys.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 用户关注关联表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("con_user_follow")
public class ConUserFollow extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private long followUserId;
    private long beFollowUserId;
}
