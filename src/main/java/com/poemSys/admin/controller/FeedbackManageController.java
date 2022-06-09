package com.poemSys.admin.controller;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poemSys.admin.bean.Form.IdForm;
import com.poemSys.admin.bean.Form.IdsForm;
import com.poemSys.admin.bean.Form.ReplyFeedBackForm;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysFeedback;
import com.poemSys.common.entity.basic.SysMessage;
import com.poemSys.common.service.SysFeedbackService;
import com.poemSys.common.service.SysMessageService;
import com.poemSys.user.bean.WebsocketMsg;
import com.poemSys.user.service.general.GetLoginSysUserService;
import com.poemSys.user.service.general.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户反馈后台管理
 */
@RestController
@RequestMapping("/api/admin")
public class FeedbackManageController
{
    @Autowired
    SysFeedbackService sysFeedbackService;

    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    @Autowired
    SysMessageService sysMessageService;

    @Autowired
    WebSocketService webSocketService;

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/partFeedbackList/{page}/{size}")
    public Result partFeedbackList(@PathVariable("page") Integer page,
                                   @PathVariable("size") Integer size)
    {
        Page<SysFeedback> feedbackPage = new Page<>(page, size);
        Page<SysFeedback> pageAns = sysFeedbackService.page(feedbackPage);
        return new Result(0, "分页获取反馈列表成功", pageAns);
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/replyFeedback")
    public Result replyFeedback(@RequestBody ReplyFeedBackForm replyFeedBackForm)
    {
        long feedbackId = replyFeedBackForm.getFeedbackId();
        String replyContent = replyFeedBackForm.getReplyContent();

        SysFeedback sysFeedback = sysFeedbackService.getById(feedbackId);
        if(sysFeedback==null)
            return new Result(1, "反馈不存在,id"+feedbackId, null);
        sysFeedback.setState(true);
        sysFeedback.setSolveTime(LocalDateTime.now());
        sysFeedback.setProcessorUserId(getLoginSysUserService.getSysUser().getId());
        sysFeedback.setFeedbackInfo(replyContent);
        sysFeedbackService.updateById(sysFeedback);

        String msgContent = "对您的反馈'"+sysFeedback.getContent()+"'进行了回复:\n"
                +replyContent;
        //推送消息给用户
        sysMessageService.save(new SysMessage(sysFeedback.getApplyUserId(), false, 4,
                0, msgContent, 0, true, LocalDateTime.now(),
                UUID.randomUUID().toString(),0));

        webSocketService.sendMessageOnServer(sysFeedback.getApplyUserId(), new WebsocketMsg(
                1, 0, sysFeedback.getApplyUserId(), msgContent
        ));

        return new Result(0, "反馈回复成功,id:"+feedbackId, null);
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/deleteFeedback")
    public Result deleteFeedback(@RequestBody IdForm idForm)
    {
        long feedbackId = idForm.getId();
        SysFeedback sysFeedback = sysFeedbackService.getById(feedbackId);
        if(sysFeedback==null)
            return new Result(1, "反馈不存在,id"+feedbackId, null);
        sysFeedbackService.removeById(feedbackId);
        return new Result(0, "反馈删除成功,id:"+feedbackId, null);
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/deleteFeedbacks")
    public Result deleteFeedbacks(@RequestBody IdsForm idsForm)
    {
        List<IdForm> ids = idsForm.getIds();
        ids.forEach(id->{
            if(sysFeedbackService.getById(id)!=null)
                sysFeedbackService.removeById(id);
        });
        return new Result(0, "反馈批量删除成功", null);
    }
}
