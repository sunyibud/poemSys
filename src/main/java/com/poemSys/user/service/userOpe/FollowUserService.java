package com.poemSys.user.service.userOpe;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.poemSys.admin.bean.Form.IdForm;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysMessage;
import com.poemSys.common.entity.connection.ConUserFollow;
import com.poemSys.common.service.ConUserFollowService;
import com.poemSys.common.service.SysMessageService;
import com.poemSys.common.service.SysUserService;
import com.poemSys.user.bean.WebsocketMsg;
import com.poemSys.user.service.general.GetLoginSysUserService;
import com.poemSys.user.service.general.UpdateRedisSysUserService;
import com.poemSys.user.service.general.WebSocketService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FollowUserService
{
    @Autowired
    ConUserFollowService conUserFollowService;

    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    @Autowired
    SysMessageService sysMessageService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    WebSocketService webSocketService;

    @Autowired
    UpdateRedisSysUserService updateRedisSysUserService;

    public Result follow(IdForm idForm)
    {
        long followUserId = getLoginSysUserService.getSysUser().getId();
        long beFollowUserId = idForm.getId();

        if(followUserId==beFollowUserId)
            return new Result(1, "不能关注自己", null);

        ConUserFollow one = conUserFollowService.getOne(new QueryWrapper<ConUserFollow>()
                .eq("follow_user_id", followUserId).eq("be_follow_user_id", beFollowUserId));

        if(one == null) //关注
        {
            conUserFollowService.save(new ConUserFollow(followUserId, beFollowUserId));

            //消息处理(如果存在改消息则无需重复新建，直接修改已读状态state和建立时间）
            SysMessage msg = sysMessageService.getOne(new QueryWrapper<SysMessage>()
                    .eq("user_id", beFollowUserId).eq("other_user_id", followUserId)
                    .eq("type", 2));
            if(msg==null)
                sysMessageService.save(new SysMessage(beFollowUserId, false, 2, 0,
                        null, followUserId, true, LocalDateTime.now(),
                        UUID.randomUUID().toString(), 0));
            else
            {
                msg.setReceiveTime(LocalDateTime.now());
                msg.setState(false);
                sysMessageService.updateById(msg);
            }
            webSocketService.sendMessageOnServer(beFollowUserId, new WebsocketMsg(2, 0, 0, "用户"+followUserId+"关注了你"));

            updateRedisSysUserService.updateById(beFollowUserId);
            updateRedisSysUserService.update();

            long newFansNum = sysUserService.getSysUserById(beFollowUserId).getFansNum();
            NewState newState = new NewState(true, newFansNum);

            return new Result(0, "用户"+followUserId+"(id)成功关注用户"+beFollowUserId+"(id)", newState);
        }
        else    //取消关注
        {
            conUserFollowService.removeById(one);

            updateRedisSysUserService.updateById(beFollowUserId);
            updateRedisSysUserService.update();

            long newFansNum = sysUserService.getSysUserById(beFollowUserId).getFansNum();
            NewState newState = new NewState(false, newFansNum);

            return new Result(0, "用户"+followUserId+"(id)成功取消关注用户"+beFollowUserId+"(id)", newState);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class NewState implements Serializable
    {
        private static final long serialVersionUID = 1L;

        private boolean isFollow;
        private long newFansNum;
    }
}
