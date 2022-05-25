package com.poemSys.user.service.userOpe;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.poemSys.admin.bean.Form.IdForm;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.connection.ConUserPoemCollect;
import com.poemSys.common.service.ConUserPoemCollectService;
import com.poemSys.common.service.SysPoemService;
import com.poemSys.user.service.general.GetLoginSysUserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class PoemCollectService
{
    @Autowired
    ConUserPoemCollectService conUserPoemCollectService;

    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    @Autowired
    SysPoemService sysPoemService;

    public Result ope(IdForm idForm)
    {
        Long userId = getLoginSysUserService.getSysUser().getId();
        long poemId = idForm.getId();
        ConUserPoemCollect con = conUserPoemCollectService.getOne(new QueryWrapper<ConUserPoemCollect>()
                .eq("user_id", userId).eq("poem_id", poemId));
        boolean isCollect = false;

        if(con==null)   //未收藏
        {
            conUserPoemCollectService.save(new ConUserPoemCollect(userId, poemId));
            isCollect = true;
        }
        else
            conUserPoemCollectService.removeById(con);
        //websocket

        NewState newState = new NewState(isCollect, sysPoemService.getById(poemId).getCollectNum());

        return new Result(0, "用户"+userId+"(id)收藏/取消收藏古诗词"+poemId+"(id)成功", newState);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class NewState implements Serializable
    {
        private static final long serialVersionUID = 1L;

        private boolean isCollect;
        private int collectNum;
    }
}
