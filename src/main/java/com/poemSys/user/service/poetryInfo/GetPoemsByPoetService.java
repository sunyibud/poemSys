package com.poemSys.user.service.poetryInfo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poemSys.admin.bean.PageListRes;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysPoem;
import com.poemSys.common.entity.basic.SysPoet;
import com.poemSys.common.entity.connection.ConPoetPoem;
import com.poemSys.common.service.ConPoetPoemService;
import com.poemSys.common.service.SysPoemService;
import com.poemSys.common.service.SysPoetService;
import com.poemSys.user.bean.Form.PageByIdForm;
import com.poemSys.user.bean.PoemPageAns;
import com.poemSys.user.service.general.PoemPageAnsProService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetPoemsByPoetService
{
    @Autowired
    SysPoetService sysPoetService;

    @Autowired
    ConPoetPoemService conPoetPoemService;

    @Autowired
    SysPoemService sysPoemService;

    @Autowired
    PoemPageAnsProService poemPageAnsProService;

    public Result get(PageByIdForm pageByIdForm)
    {
        long poetId = pageByIdForm.getId();
        long page = pageByIdForm.getPage();
        long size = pageByIdForm.getSize();
        SysPoet sysPoet = sysPoetService.getOne(new QueryWrapper<SysPoet>()
                .eq("id", poetId));
        if(sysPoet==null)
            return new Result(1, "找不到该诗人,id:"+poetId, null);
        String poetName = sysPoet.getName();
        List<Long> poemIds = new ArrayList<>();
        List<ConPoetPoem> list = conPoetPoemService.list(new QueryWrapper<ConPoetPoem>()
                .eq("poet_id", poetId));
        list.forEach(l -> poemIds.add(l.getPoemId()));
        if (poemIds.isEmpty())
            return new Result(0, "分页获取作者为"+poetName+"的古诗词列表成功,共0条", new PageListRes(
                    0L, size, page, 0L, null
            ));
        Page<SysPoem> poemPage = new Page<>(page, size);
        Page<SysPoem> pageAns = sysPoemService.page(poemPage,
                new QueryWrapper<SysPoem>().in("id", poemIds));
        PoemPageAns res = poemPageAnsProService.pro(pageAns);
        return new Result(0, "分页获取作者为"+poetName+"的古诗词列表成功,共"+pageAns.getTotal()+"条", res);
    }
}
