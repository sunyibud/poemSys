package com.poemSys.common.entity.connection;

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
@TableName("con_message_letter")
public class ConMessageLetter extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private long messageId;
    private long letterId;
}
