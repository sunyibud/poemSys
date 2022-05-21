package com.poemSys.user.service.userOpe;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.poemSys.admin.bean.Form.IdForm;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.connection.ConUserPoemLike;
import com.poemSys.common.service.ConUserPoemLikeService;
import com.poemSys.common.service.SysPoemService;
import com.poemSys.common.service.general.GetLoginSysUserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 点赞和取消点赞
 */
@Service
public class PoemLikeService
{
    @Autowired
    ConUserPoemLikeService conUserPoemLikeService;

    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    @Autowired
    SysPoemService sysPoemService;

    public Result ope(IdForm idForm)
    {
        Long userId = getLoginSysUserService.getSysUser().getId();
        long poemId = idForm.getId();
        ConUserPoemLike con = conUserPoemLikeService.getOne(new QueryWrapper<ConUserPoemLike>()
                .eq("user_id", userId).eq("poem_id", poemId));
        boolean isLike = false;

        if(con==null)   //没点过赞
        {
            conUserPoemLikeService.save(new ConUserPoemLike(userId, poemId));
            isLike = true;
        }
        else
            conUserPoemLikeService.removeById(con);
        //websocket

        NewState newState = new NewState(isLike, sysPoemService.getById(poemId).getLikeNum());

        return new Result(0, "用户"+userId+"(id)点赞/取消点赞古诗词"+poemId+"(id)成功", newState);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class NewState implements Serializable
    {
        private static final long serialVersionUID = 1L;

        private boolean isLike;
        private int likeNum;
    }
}
