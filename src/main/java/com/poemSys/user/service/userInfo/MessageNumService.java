package com.poemSys.user.service.userInfo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysMessage;
import com.poemSys.common.service.SysMessageService;
import com.poemSys.user.service.general.GetLoginSysUserService;
import com.poemSys.user.bean.MessageNum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 获取各个种类的未读消息的数量(私信数量为与不同用户有新消息的用户种类)
 */
@Service
public class MessageNumService
{
    @Autowired
    SysMessageService sysMessageService;

    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    public Result get()
    {
        Long userId = getLoginSysUserService.getSysUser().getId();
        List<SysMessage> messages = sysMessageService.list(new QueryWrapper<SysMessage>()
                .eq("user_id", userId));
        int comment=0, newFans=0, likeOrCollect=0, sysMessage=0, letter=0;
        for (SysMessage message : messages)
        {
            int type = message.getType();
            switch (type){
                case 1 :{
                    if(!message.isState())
                        comment++;
                    break;
                }
                case 2 :{
                    if(!message.isState())
                        newFans++;
                    break;
                }
                case 3 :{
                    if(!message.isState())
                        likeOrCollect++;
                    break;
                }
                case 4 :{
                    if(!message.isState())
                        sysMessage++;
                    break;
                }
                case 5 :{
                    if(!message.isState())
                        letter++;
                    break;
                }
            }
        }
        int total = comment+newFans+likeOrCollect+sysMessage+letter;
        MessageNum messageNum = new MessageNum(total, comment, newFans, likeOrCollect, sysMessage, letter);
        return new Result(0 ,"各类型的未读消息数量获取成功,共"+total+"条", messageNum);
    }
}
