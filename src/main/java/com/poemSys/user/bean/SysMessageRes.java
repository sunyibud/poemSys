package com.poemSys.user.bean;

import com.poemSys.common.entity.basic.SysComment;
import com.poemSys.common.entity.basic.SysPost;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysMessageRes implements Serializable
{
    private static final long serialVersionUID = 1L;

    private long id;//这条消息的id
    private boolean state;//状态(已读/未读)
    private int type;     //类型(1:comment,2:newFans,3:likeOrCollect,4:sysMessage,5:letter
    private SysPost postInfo;  //type为1和3时表示帖子
    private String content; //消息或评论的内容
    private UserInfo otherUserInfo;  //type: 1表示评论的用户 2表示新增的粉丝 3点赞或收藏的人 4推送该消息的管理员 5与之私信的人
    private LocalDateTime receiveTime; //消息接受的时间
    private SysComment beReplyCommentInfo;//type为1,且为回复二级评论时,表示自己的哪条评论被回复
}
