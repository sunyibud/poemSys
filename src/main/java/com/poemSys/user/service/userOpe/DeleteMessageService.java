package com.poemSys.user.service.userOpe;

import com.poemSys.admin.bean.Form.IdForm;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysMessage;
import com.poemSys.common.service.ConMessageLetterService;
import com.poemSys.common.service.SysLetterService;
import com.poemSys.common.service.SysMessageService;
import com.poemSys.user.service.general.GetLoginSysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 在私信列表中删除私信
 */
@Slf4j
@Service
public class DeleteMessageService
{
    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    @Autowired
    ConMessageLetterService conMessageLetterService;

    @Autowired
    SysMessageService sysMessageService;

    @Autowired
    SysLetterService sysLetterService;


    public Result delete(IdForm idForm)
    {
        Long loginUserId = getLoginSysUserService.getSysUser().getId();

        long msgId = idForm.getId();
        SysMessage sysMessage = sysMessageService.getById(msgId);
        if(sysMessage==null)
            return new Result(1, "消息不存在,id:"+msgId, null);
        if(sysMessage.getUserId()!=loginUserId)
            return new Result(1, "权限不足,该条消息不属于用户"+loginUserId+"(id)", null);

        int type = sysMessage.getType();
        if(type!=5) //不是私信消息
        {
            sysMessageService.removeById(msgId);
            return new Result(0, "普通消息删除成功,id:"+msgId, null);
        }
        //让该条私信不在列表中显示
        sysMessage.setShow(false);
        sysMessageService.updateById(sysMessage);
        return new Result(0, "私信消息删除成功,id:"+msgId, null);
    }
}
