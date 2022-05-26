package com.poemSys.user.service.userOpe;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysLetter;
import com.poemSys.common.entity.basic.SysMessage;
import com.poemSys.common.entity.connection.ConMessageLetter;
import com.poemSys.common.service.ConMessageLetterService;
import com.poemSys.common.service.SysLetterService;
import com.poemSys.common.service.SysMessageService;
import com.poemSys.user.service.forum.ContentCheckService;
import com.poemSys.user.service.general.GetLoginSysUserService;
import com.poemSys.user.bean.Form.SendLetterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SendLetterService
{
    @Autowired
    SysMessageService sysMessageService;

    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    @Autowired
    ConMessageLetterService conMessageLetterService;

    @Autowired
    SysLetterService sysLetterService;

    @Autowired
    ContentCheckService contentCheckService;

    public Result send(SendLetterForm sendLetterForm)
    {
        Long userId = getLoginSysUserService.getSysUser().getId();
        long toUserId = sendLetterForm.getToUserId();
        String content = sendLetterForm.getContent();

        //替换消息内容敏感词
        content = contentCheckService.KMPReplace(content);

        //查看是否以前发过消息，表中是否已经存在私信数据(收发双方都需要存在),不存在就新建
        SysMessage mySysMessage = sysMessageService.getOne(new QueryWrapper<SysMessage>()
                .eq("user_id", userId).eq("type", 5)
                .eq("letter_user_id", toUserId));
        SysMessage toSysMessage = sysMessageService.getOne(new QueryWrapper<SysMessage>()
                .eq("user_id", toUserId).eq("type", 5)
                .eq("user_id", toUserId));
        String mySysMsgUuid = UUID.randomUUID().toString();
        String toSysMsgUuid = UUID.randomUUID().toString();
        if(mySysMessage==null)
        {
            sysMessageService.save(new SysMessage(userId, true, 5,
                    0, 0, null, toUserId, true, LocalDateTime.now(), mySysMsgUuid, 0));
            mySysMessage = sysMessageService.getOne(new QueryWrapper<SysMessage>()
                .eq("uuid", mySysMsgUuid));
        }
        if(toSysMessage==null)
        {
            sysMessageService.save(new SysMessage(toUserId, false, 5,
                    0, 0, null, userId, true, LocalDateTime.now(), toSysMsgUuid, 0));
            toSysMessage = sysMessageService.getOne(new QueryWrapper<SysMessage>()
                .eq("uuid", toSysMsgUuid));
        }
        //设置接收方的消息为未读
        toSysMessage.setState(false);
        //设置该条私信框在双方的列表中都显示
        mySysMessage.set_show(true);
        toSysMessage.set_show(true);
        sysMessageService.updateById(toSysMessage);
        sysMessageService.updateById(mySysMessage);

        //新建本条私信消息内容,并与双方的sysMessage关联
        String sysLetterUuid = UUID.randomUUID().toString();
        sysLetterService.save(new SysLetter(userId, toUserId, content,
                LocalDateTime.now(), false, sysLetterUuid));
        long letterId = sysLetterService.getOne(new QueryWrapper<SysLetter>()
                .eq("uuid", sysLetterUuid)).getId();
        conMessageLetterService.save(new ConMessageLetter(mySysMessage.getId(), letterId));
        conMessageLetterService.save(new ConMessageLetter(toSysMessage.getId(), letterId));
        return new Result(0, "用户"+userId+"(id)向用户"+toUserId+"(id)发送私信成功", LocalDateTime.now());
    }
}
