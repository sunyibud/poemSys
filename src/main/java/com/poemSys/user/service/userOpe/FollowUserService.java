package com.poemSys.user.service.userOpe;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.poemSys.admin.bean.Form.IdForm;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysMessage;
import com.poemSys.common.entity.connection.ConUserFollow;
import com.poemSys.common.service.ConUserFollowService;
import com.poemSys.common.service.SysMessageService;
import com.poemSys.user.service.general.GetLoginSysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Result follow(IdForm idForm)
    {
        long followUserId = getLoginSysUserService.getSysUser().getId();
        long beFollowUserId = idForm.getId();

        ConUserFollow one = conUserFollowService.getOne(new QueryWrapper<ConUserFollow>()
                .eq("follow_user_id", followUserId).eq("be_follow_user_id", beFollowUserId));

        boolean isFollow = false;
        if(one == null) //关注
        {
            conUserFollowService.save(new ConUserFollow(followUserId, beFollowUserId));

            //消息处理
            sysMessageService.save(new SysMessage(beFollowUserId, false, 2, 0,
                    followUserId, null, 0, true, LocalDateTime.now(),
                    UUID.randomUUID().toString(), 0));
        }
        else    //取消关注
        {
            conUserFollowService.removeById(one);
        }
        return new Result(0, "用户"+followUserId+"(id)成功关注用户"+beFollowUserId+"(id)", isFollow);
    }
}
