package com.poemSys.user.service.poetryInfo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poemSys.admin.bean.PageListRes;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysPoem;
import com.poemSys.common.entity.basic.SysTag;
import com.poemSys.common.entity.connection.ConTagPoem;
import com.poemSys.common.service.ConTagPoemService;
import com.poemSys.common.service.SysPoemService;
import com.poemSys.common.service.SysTagService;
import com.poemSys.user.bean.Form.PageByIdForm;
import com.poemSys.user.bean.PoemPageAns;
import com.poemSys.user.service.general.PoemPageAnsPro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetPoemsByTagService
{
    @Autowired
    SysTagService sysTagService;

    @Autowired
    ConTagPoemService conTagPoemService;

    @Autowired
    SysPoemService sysPoemService;

    @Autowired
    PoemPageAnsPro poemPageAnsPro;

    public Result get(PageByIdForm pageByIdForm)
    {
        long tagId = pageByIdForm.getId();
        long page = pageByIdForm.getPage();
        long size = pageByIdForm.getSize();
        SysTag sysTag = sysTagService.getOne(new QueryWrapper<SysTag>()
                .eq("id", tagId));
        if(sysTag==null)
            return new Result(1, "找不到该标签,id:"+tagId, null);
        String tagName = sysTag.getName();
        List<Long> poemIds = new ArrayList<>();
        List<ConTagPoem> list = conTagPoemService.list(new QueryWrapper<ConTagPoem>()
                .eq("tag_id", tagId));
        list.forEach(l -> {
            poemIds.add(l.getPoemId());
        });
        if (poemIds.isEmpty())
            return new Result(0, "分页获取类型为"+tagName+"的古诗词列表成功,共0条", new PageListRes(
                    0L, size, page, 0L, null
            ));
        Page<SysPoem> poemPage = new Page<>(page, size);
        Page<SysPoem> pageAns = sysPoemService.page(poemPage,
                new QueryWrapper<SysPoem>().in("id", poemIds));
        PoemPageAns res = poemPageAnsPro.pro(pageAns);
        return new Result(0, "分页获取类型为"+tagName+"的古诗词列表成功,共"+pageAns.getTotal()+"条", res);
    }
}
