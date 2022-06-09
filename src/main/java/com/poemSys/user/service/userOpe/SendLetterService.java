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
import com.poemSys.user.bean.WebsocketMsg;
import com.poemSys.user.service.forum.ContentCheckService;
import com.poemSys.user.service.general.GetLoginSysUserService;
import com.poemSys.user.bean.Form.SendLetterForm;
import com.poemSys.user.service.general.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    @Autowired
    WebSocketService webSocketService;

    public Result send(SendLetterForm sendLetterForm)
    {
        Long loginUserId = getLoginSysUserService.getSysUser().getId();
        long toUserId = sendLetterForm.getToUserId();
        String content = sendLetterForm.getContent();

        if(loginUserId==toUserId)
            return new Result(1, "不能给自己发送私信", null);

        //替换消息内容敏感词
        content = contentCheckService.KMPReplace(content);

        //websocket
        webSocketService.sendMessageOnServer(toUserId, new WebsocketMsg(2, loginUserId, toUserId, content));

        //查看是否以前发过消息，表中是否已经存在私信数据(收发双方都需要存在),不存在就新建
        SysMessage mySysMessage = sysMessageService.getOne(new QueryWrapper<SysMessage>()
                .eq("user_id", loginUserId).eq("type", 5)
                .eq("other_user_id", toUserId));
        SysMessage toSysMessage = sysMessageService.getOne(new QueryWrapper<SysMessage>()
                .eq("user_id", toUserId).eq("type", 5)
                .eq("other_user_id", loginUserId));
        String mySysMsgUuid = UUID.randomUUID().toString();
        String toSysMsgUuid = UUID.randomUUID().toString();
        if(mySysMessage==null)
        {
            sysMessageService.save(new SysMessage(loginUserId, true, 5,
                    0, null, toUserId, true, LocalDateTime.now(), mySysMsgUuid, 0));
            mySysMessage = sysMessageService.getOne(new QueryWrapper<SysMessage>()
                .eq("uuid", mySysMsgUuid));
        }
        if(toSysMessage==null)
        {
            sysMessageService.save(new SysMessage(toUserId, false, 5,
                    0, null, loginUserId, true, LocalDateTime.now(), toSysMsgUuid, 0));
            toSysMessage = sysMessageService.getOne(new QueryWrapper<SysMessage>()
                .eq("uuid", toSysMsgUuid));
        }

        //设置接收方的消息为未读
        toSysMessage.setState(false);

        //设置该条私信框在双方的列表中都显示,并更新接受时间，以便于之后展示排序时把最新接受到的放在最前面
        mySysMessage.setShow(true);
        toSysMessage.setShow(true);
        mySysMessage.setReceiveTime(LocalDateTime.now());
        toSysMessage.setReceiveTime(LocalDateTime.now());
        sysMessageService.updateById(toSysMessage);
        sysMessageService.updateById(mySysMessage);

        //新建本条私信消息内容,并与双方的sysMessage关联
        String sysLetterUuid = UUID.randomUUID().toString();
        sysLetterService.save(new SysLetter(loginUserId, toUserId, content,
                LocalDateTime.now(), false, sysLetterUuid));
        long letterId = sysLetterService.getOne(new QueryWrapper<SysLetter>()
                .eq("uuid", sysLetterUuid)).getId();
        conMessageLetterService.save(new ConMessageLetter(mySysMessage.getId(), letterId));
        conMessageLetterService.save(new ConMessageLetter(toSysMessage.getId(), letterId));
        return new Result(0, "用户"+loginUserId+"(id)向用户"+toUserId+"(id)发送私信成功",
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
    }
}
