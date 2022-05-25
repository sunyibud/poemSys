package com.poemSys.user.service.userInfo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poemSys.admin.bean.PageListRes;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysPoem;
import com.poemSys.common.entity.connection.ConUserPoemCollect;
import com.poemSys.common.service.ConUserPoemCollectService;
import com.poemSys.common.service.SysPoemService;
import com.poemSys.user.service.general.GetLoginSysUserService;
import com.poemSys.user.bean.PoemPageAns;
import com.poemSys.user.service.general.PoemPageAnsProService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyPoemCollectService
{
    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    @Autowired
    ConUserPoemCollectService conUserPoemCollectService;

    @Autowired
    SysPoemService sysPoemService;

    @Autowired
    PoemPageAnsProService poemPageAnsProService;

    public Result getPageList(Integer page, Integer size)
    {
        Long id = getLoginSysUserService.getSysUser().getId();
        Page<SysPoem> poemPage = new Page<>(page, size);

        List<ConUserPoemCollect> list = conUserPoemCollectService.list(new QueryWrapper<ConUserPoemCollect>()
                .eq("user_id", id));
        List<Long> poemIds = new ArrayList<>();
        list.forEach(l-> poemIds.add(l.getPoemId()));
        if(poemIds.isEmpty())
            return new Result(0, "分页获取用户收藏古诗词列表成功,共0条", new PageListRes(
                    0L, size.longValue(), page.longValue(), 0L, null
            ));
        Page<SysPoem> pageAns = sysPoemService.page(poemPage, new QueryWrapper<SysPoem>()
                .in("id", poemIds));
        PoemPageAns finalAns = poemPageAnsProService.pro(pageAns);
        return new Result(0, "分页获取用户收藏古诗词列表成功,共"+finalAns.getTotal()+"条", finalAns);
    }
}
