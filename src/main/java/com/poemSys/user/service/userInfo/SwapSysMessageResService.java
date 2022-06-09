package com.poemSys.user.service.userInfo;

import com.poemSys.common.entity.basic.SysComment;
import com.poemSys.common.entity.basic.SysMessage;
import com.poemSys.common.entity.basic.SysPost;
import com.poemSys.common.entity.basic.SysUser;
import com.poemSys.common.service.SysCommentService;
import com.poemSys.common.service.SysPostService;
import com.poemSys.common.service.SysUserService;
import com.poemSys.user.bean.SysMessageRes;
import com.poemSys.user.bean.UserInfo;
import com.poemSys.user.service.general.SwapUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 将sysMessage转化为sysMessageRes，显示更加详细的内容返回给前端
 */
@Service
public class SwapSysMessageResService
{
    @Autowired
    SwapUserInfoService swapUserInfoService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysPostService sysPostService;

    @Autowired
    SysCommentService sysCommentService;

    public SysMessageRes swap(SysMessage sysMessage)
    {
        SysPost postInfo = sysPostService.getById(sysMessage.getPostId());
        UserInfo otherUserInfo = null;
        if(sysMessage.getOtherUserId()!=0)
        {
            SysUser letterUser = sysUserService.getSysUserById(sysMessage.getOtherUserId());
            otherUserInfo = swapUserInfoService.swap(letterUser);
        }
        SysComment beReplyComment = sysCommentService.getById(sysMessage.getBeReplyCommentId());
        return new SysMessageRes(sysMessage.getId(), sysMessage.isState(),
                sysMessage.getType(), postInfo, sysMessage.getContent(),
                otherUserInfo, sysMessage.getReceiveTime(), beReplyComment);
    }
}
