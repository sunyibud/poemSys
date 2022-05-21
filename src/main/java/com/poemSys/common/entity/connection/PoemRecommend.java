package com.poemSys.common.entity.connection;

import com.baomidou.mybatisplus.annotation.TableName;
import com.poemSys.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 首页推荐古诗词（所有信息详细的古诗词id)
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("poem_recommend")
public class PoemRecommend extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private long poemId;
}
