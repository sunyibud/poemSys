package com.poemSys.user.service.userOpe;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.poemSys.admin.bean.Form.IdForm;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysLetter;
import com.poemSys.common.entity.basic.SysMessage;
import com.poemSys.common.entity.connection.ConMessageLetter;
import com.poemSys.common.service.ConMessageLetterService;
import com.poemSys.common.service.SysLetterService;
import com.poemSys.common.service.SysMessageService;
import com.poemSys.user.service.general.GetLoginSysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReadLetterService
{
    @Autowired
    SysMessageService sysMessageService;

    @Autowired
    SysLetterService sysLetterService;

    @Autowired
    ConMessageLetterService conMessageLetterService;

    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    public Result read(IdForm idForm)
    {
        Long loginUserId = getLoginSysUserService.getSysUser().getId();
        long sysMessageId = idForm.getId();

        //将sysMessage设为已读
        SysMessage sysMessage = sysMessageService.getById(sysMessageId);
        if(sysMessage==null)
            return new Result(1, "未找到该条消息，id"+sysMessageId, null);
        if(sysMessage.getType() != 5)
            return new Result(1, "该条消息不是私信消息，所属的type:"+sysMessage.getType(), null);
        sysMessage.setState(true);
        sysMessageService.updateById(sysMessage);

        //将该条私信消息所对应的所有由对方发出的具体内容改为已读
        List<ConMessageLetter> con = conMessageLetterService.list(new QueryWrapper<ConMessageLetter>()
                .eq("message_id", sysMessageId));
        List<Long> letterIds = new ArrayList<>();
        con.forEach(c-> letterIds.add(c.getLetterId()));
        if(!letterIds.isEmpty())
        {
            List<SysLetter> letters = sysLetterService.list(new QueryWrapper<SysLetter>()
                    .in("id", letterIds));

            letters.forEach(l ->
            {
                if (l.getReceiveUserId() == loginUserId)
                {
                    l.setState(true);
                    sysLetterService.updateById(l);
                }
            });
        }
        return new Result(0, "消息"+sysMessageId+"(id)"+"已设为已读(私信消息)", null);
    }
}
