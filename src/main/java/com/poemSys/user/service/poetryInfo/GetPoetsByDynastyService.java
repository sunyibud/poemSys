package com.poemSys.user.service.poetryInfo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poemSys.admin.bean.PageListRes;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysDynasty;
import com.poemSys.common.entity.basic.SysPoet;
import com.poemSys.common.entity.connection.ConDynastyPoet;
import com.poemSys.common.service.ConDynastyPoetService;
import com.poemSys.common.service.SysDynastyService;
import com.poemSys.common.service.SysPoetService;
import com.poemSys.user.bean.Form.PageByIdForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetPoetsByDynastyService
{
    @Autowired
    SysDynastyService sysDynastyService;

    @Autowired
    ConDynastyPoetService conDynastyPoetService;

    @Autowired
    SysPoetService sysPoetService;

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
        List<Long> poetIds = new ArrayList<>();
        List<ConDynastyPoet> list = conDynastyPoetService.list(new QueryWrapper<ConDynastyPoet>()
                .eq("dynasty_id", dynastyId));
        list.forEach(l -> {
            poetIds.add(l.getPoetId());
        });
        if (poetIds.isEmpty())
            return new Result(0, "分页获取朝代为"+dynastyName+"的诗人列表成功,共0条", new PageListRes(
                    0L, size, page, 0L, null
            ));
        Page<SysPoet> poetPage = new Page<>(page, size);
        Page<SysPoet> pageAns = sysPoetService.page(poetPage,
                new QueryWrapper<SysPoet>().in("id", poetIds));
        return new Result(0, "分页获取朝代为"+dynastyName+"的诗人列表成功,共"+pageAns.getTotal()+"条", pageAns);
    }
}
