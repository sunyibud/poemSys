package com.poemSys.common.entity.connection;

import com.baomidou.mybatisplus.annotation.TableName;
import com.poemSys.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 首页推荐的诗人id关联表（信息详细的诗人）
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("poet_recommend")
public class PoetRecommend extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private long poetId;
}
