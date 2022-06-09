package com.poemSys.user.service.userInfo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysLetter;
import com.poemSys.common.entity.basic.SysMessage;
import com.poemSys.common.entity.basic.SysUser;
import com.poemSys.common.entity.connection.ConMessageLetter;
import com.poemSys.common.service.ConMessageLetterService;
import com.poemSys.common.service.SysLetterService;
import com.poemSys.common.service.SysMessageService;
import com.poemSys.common.service.SysUserService;
import com.poemSys.user.bean.SysLetterRes;
import com.poemSys.user.bean.SysMessageRes;
import com.poemSys.user.bean.UserInfo;
import com.poemSys.user.service.general.GetLoginSysUserService;
import com.poemSys.user.bean.LetterMessage;
import com.poemSys.user.service.general.SwapUserInfoService;
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

    @Autowired
    SwapSysMessageResService swapSysMessageResService;

    @Autowired
    SwapSysLetterResService swapSysLetterResService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SwapUserInfoService swapUserInfoService;

    public Result get(Integer type)
    {
        if(type>5||type<1)
            return new Result(-3, "消息编码有误，不在1~5内", null);
        String[] msgEnum = {"", "评论", "新增粉丝", "赞和收藏", "系统通知", "私信"};
        Long loginUserId = getLoginSysUserService.getSysUser().getId();
        List<SysMessage> messages = sysMessageService.list(new QueryWrapper<SysMessage>()
                .eq("user_id", loginUserId).orderByDesc("receive_time"));
        List<SysMessageRes> resList = new ArrayList<>();
        switch (type){
            case 1 :{//comment
                messages.forEach(m->{
                    if(m.getType()==1)
                        resList.add(swapSysMessageResService.swap(m));
                });
                break;
            }
            case 2 :{//newFans
                messages.forEach(m->{
                    if(m.getType()==2)
                        resList.add(swapSysMessageResService.swap(m));
                });
                break;
            }
            case 3 :{//likeOrCollect
                messages.forEach(m->{
                    if(m.getType()==3)
                        resList.add(swapSysMessageResService.swap(m));
                });
                break;
            }
            case 4 :{//sysMessage
                messages.forEach(m->{
                    if(m.getType()==4)
                        resList.add(swapSysMessageResService.swap(m));
                });
                break;
            }
            case 5 :{//letter
                List<LetterMessage> res = new ArrayList<>();
                for (SysMessage m : messages)
                {
                    if(m.getType()==5&&m.isShow())  //需要在用户列表中显示
                    {
                        //找到与该条(私信)消息(与某个用户的)关联的所有的具体私信内容
                        List<ConMessageLetter> con = conMessageLetterService.list(new QueryWrapper<ConMessageLetter>()
                                .eq("message_id", m.getId()));

                        List<SysLetterRes> sysLetterResList = new ArrayList<>();
                        int newNum = 0;

                        if(!con.isEmpty())   //这条私信框中含有消息
                        {
                            List<Long> letterIds = new ArrayList<>();
                            con.forEach(e -> letterIds.add(e.getLetterId()));
                            List<SysLetter> sysLetters = sysLetterService.list(new QueryWrapper<SysLetter>()
                                    .in("id", letterIds));

                            //获取未读的消息的数量并转化sysLetter为sysLetterRes
                            for (SysLetter sysLetter : sysLetters)
                            {
                                sysLetterResList.add(swapSysLetterResService.swap(sysLetter));
                                if (sysLetter.getReceiveUserId() == loginUserId && !sysLetter.isState())
                                    newNum++;
                            }
                        }
                        //将该条私信(与某个用户的)的内容封装好加入到返回列表中
                        SysUser toUser = sysUserService.getSysUserById(m.getOtherUserId());
                        UserInfo toUserInfo = swapUserInfoService.swap(toUser);
                        res.add(new LetterMessage(m.getId(), toUserInfo, newNum, sysLetterResList));
                    }
                }
                return new Result(0, "私信列表获取成功", res);
            }
        }
        //将除私信外所有种类的所有消息改为已读(发起获取请求后就代表点击了该种类的消息，此时改为已读)
        //读每个不同用户的私信时，会发送请求/ope/readLetter，表示用户点击了该条私信（此时改为已读）
        resList.forEach(e->{
            SysMessage byId = sysMessageService.getById(e.getId());
            byId.setState(true);
            sysMessageService.updateById(byId);
        });
        return new Result(0, resList.size()+"条"+msgEnum[type]+"消息获取成功", resList);
    }
}
