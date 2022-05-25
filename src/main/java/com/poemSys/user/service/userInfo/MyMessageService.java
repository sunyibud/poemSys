package com.poemSys.user.service.userInfo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysLetter;
import com.poemSys.common.entity.basic.SysMessage;
import com.poemSys.common.entity.connection.ConMessageLetter;
import com.poemSys.common.service.ConMessageLetterService;
import com.poemSys.common.service.SysLetterService;
import com.poemSys.common.service.SysMessageService;
import com.poemSys.user.service.general.GetLoginSysUserService;
import com.poemSys.user.bean.LetterMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取我的所有消息
 */
@Service
public class MyMessageService
{
    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    @Autowired
    SysMessageService sysMessageService;

    @Autowired
    SysLetterService sysLetterService;

    @Autowired
    ConMessageLetterService conMessageLetterService;

    public Result get(Integer type)
    {
        if(type>5||type<1)
            return new Result(-3, "消息编码有误，不再1~5内", null);
        String[] msgEnum = {"", "评论", "新增粉丝", "赞和收藏", "系统通知", "私信"};
        Long id = getLoginSysUserService.getSysUser().getId();
        List<SysMessage> messages = sysMessageService.list(new QueryWrapper<SysMessage>()
                .eq("user_id", id));
        List<SysMessage> resList = new ArrayList<>();
        switch (type){
            case 1 :{//comment
                messages.forEach(m->{
                    if(m.getType()==1)
                        resList.add(m);
                });
                break;
            }
            case 2 :{//newFans
                messages.forEach(m->{
                    if(m.getType()==2)
                        resList.add(m);
                });
                break;
            }
            case 3 :{//likeOrCollect
                messages.forEach(m->{
                    if(m.getType()==3)
                        resList.add(m);
                });
                break;
            }
            case 4 :{//sysMessage
                messages.forEach(m->{
                    if(m.getType()==4)
                        resList.add(m);
                });
                break;
            }
            case 5 :{//letter
                List<LetterMessage> res = new ArrayList<>();
                messages.forEach(m->{
                    if(m.is_show())  //是否需要在用户列表中显示
                    {
                        //找到与该条私信(与某个用户的)关联的所有的具体内容
                        List<ConMessageLetter> con = conMessageLetterService.list(new QueryWrapper<ConMessageLetter>()
                                .eq("message_id", m.getId()));
                        List<Long> letterIds = new ArrayList<>();
                        con.forEach(e ->
                                letterIds.add(e.getLetterId()));
                        List<SysLetter> sysLetters = sysLetterService.list(new QueryWrapper<SysLetter>()
                                .in("id", letterIds));
                        //未读的消息的数量
                        int newNum = 0;
                        for (SysLetter sysLetter : sysLetters)
                        {
                            if (sysLetter.getReceiveUserId() == id && !sysLetter.isState())
                                newNum++;
                        }
                        //将该条私信(与某个用户的)的内容封装好加入到返回列表中
                        res.add(new LetterMessage(m.getId(), m.getLetterUserId(), newNum, sysLetters));
                    }
                });
                return new Result(0, "私信列表获取成功", res);
            }
        }
        //将除私信外所有种类的所有消息改为已读(发起获取请求后就代表点击了该种类的消息，此时改为已读)
        //读每个不同用户的私信时，会发送请求/ope/readLetter，表示用户点击了该条私信（此时改为已读）
        resList.forEach(e->{
            e.setState(true);
            sysMessageService.updateById(e);
        });
        return new Result(0, resList.size()+"条"+msgEnum[type]+"消息获取成功", resList);
    }
}
