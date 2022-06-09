package com.poemSys.common.entity.basic;

import com.baomidou.mybatisplus.annotation.TableName;
import com.poemSys.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 搜索框关键字推荐
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_key_word_rec")
public class SysKeyWordRec extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private String keyWord;
}
