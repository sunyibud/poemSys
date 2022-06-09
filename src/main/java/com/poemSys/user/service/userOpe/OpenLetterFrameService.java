package com.poemSys.user.service.userOpe;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.poemSys.admin.bean.Form.IdForm;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysMessage;
import com.poemSys.common.entity.basic.SysUser;
import com.poemSys.common.service.SysMessageService;
import com.poemSys.common.service.SysUserService;
import com.poemSys.user.service.general.GetLoginSysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 打开与某个用户的私信聊天框（如果没有就创建）
 */
@Service
public class OpenLetterFrameService
{
    @Autowired
    SysUserService sysUserService;

    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    @Autowired
    SysMessageService sysMessageService;

    public Result open(IdForm idForm)
    {
        SysUser loginUser = getLoginSysUserService.getSysUser();
        Long loginUserId = loginUser.getId();
        long toUserId = idForm.getId();

        if(loginUserId==toUserId)
            return new Result(1, "不能给自己发私信", null);

        SysUser toUser = sysUserService.getSysUserById(toUserId);
        if(toUser==null)
            return new Result(1, "该用户不存在,id:"+toUserId, null);

        //查看是否以前发过消息，表中是否已经存在私信数据(发送方需要存在),不存在就新建
        SysMessage mySysMessage = sysMessageService.getOne(new QueryWrapper<SysMessage>()
                .eq("user_id", loginUserId).eq("type", 5)
                .eq("other_user_id", toUserId));
        if(mySysMessage==null)
        {
            String mySysMsgUuid = UUID.randomUUID().toString();
            sysMessageService.save(new SysMessage(loginUserId, true, 5,
                     0, null, toUserId, true, LocalDateTime.now(), mySysMsgUuid, 0));
        }
        else
        {
            if(!mySysMessage.isShow())//没有显示就显示出来
            {
                mySysMessage.setReceiveTime(LocalDateTime.now());
                mySysMessage.setShow(true);
                sysMessageService.updateById(mySysMessage);
            }
        }
        return new Result(0, "与用户"+toUserId+"(id)的私信框建立成功", null);
    }
}
