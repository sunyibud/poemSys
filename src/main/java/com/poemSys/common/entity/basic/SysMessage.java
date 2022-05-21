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
@TableName("sys_message")
public class SysMessage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private long userId;  //该条消息的所有者id
    private boolean state;//状态(已读/未读)
    private int type;     //类型(1:comment,2:newFans,3:likeOrCollect,4:sysMessage,5:letter
    private long postId;  //当类型为comment和likeOrCollect 时表示帖子id
    private long fansId;  //当类型newFans时表示粉丝的id
    private String content; //消息或评论的内容
    private long letterUserId;  //当类型为letter时表示与之私信的用户id
    private boolean is_show;   //当类型为letter时表示是否在用户消息列表中显示
    private LocalDateTime receiveTime; //消息接受的时间
    private String uuid;    //消息的唯一标识
    private long commentUserId; //当类型为comment时表示对自己的评论进行回复的用户id
}
