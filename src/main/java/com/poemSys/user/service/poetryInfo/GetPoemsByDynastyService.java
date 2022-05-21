package com.poemSys.user.service.poetryInfo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poemSys.admin.bean.PageListRes;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysDynasty;
import com.poemSys.common.entity.basic.SysPoem;
import com.poemSys.common.entity.connection.ConDynastyPoem;
import com.poemSys.common.service.ConDynastyPoemService;
import com.poemSys.common.service.SysDynastyService;
import com.poemSys.common.service.SysPoemService;
import com.poemSys.user.bean.Form.PageByIdForm;
import com.poemSys.user.bean.PoemPageAns;
import com.poemSys.user.service.general.PoemPageAnsPro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetPoemsByDynastyService
{
    @Autowired
    SysDynastyService sysDynastyService;

    @Autowired
    ConDynastyPoemService conDynastyPoemService;

    @Autowired
    SysPoemService sysPoemService;

    @Autowired
    PoemPageAnsPro poemPageAnsPro;

    public Result get(PageByIdForm pageByIdForm)
    {
        long dynastyId = pageByIdForm.getId();
        long page = pageByIdForm.getPage();
        long size = pageByIdForm.getSize();
        SysDynasty sysDynasty = sysDynastyService.getOne(new QueryWrapper<SysDynasty>()
                .eq("id", dynastyId));
        if(sysDynasty==null)
            return new Result(1, "找不到该朝代,id:"+dynastyId, null);
        String dynastyName = sysDynasty.getName();
        List<Long> poemIds = new ArrayList<>();
        List<ConDynastyPoem> list = conDynastyPoemService.list(new QueryWrapper<ConDynastyPoem>()
                .eq("dynasty_id", dynastyId));
        list.forEach(l -> {
            poemIds.add(l.getPoemId());
        });
        if (poemIds.isEmpty())
            return new Result(0, "分页获取朝代为"+dynastyName+"的古诗词列表成功,共0条", new PageListRes(
                    0L, size, page, 0L, null
            ));
        Page<SysPoem> poemPage = new Page<>(page, size);
        Page<SysPoem> pageAns = sysPoemService.page(poemPage,
                new QueryWrapper<SysPoem>().in("id", poemIds));
        PoemPageAns res = poemPageAnsPro.pro(pageAns);
        return new Result(0, "分页获取朝代为"+dynastyName+"的古诗词列表成功,共"+pageAns.getTotal()+"条", res);
    }
}
