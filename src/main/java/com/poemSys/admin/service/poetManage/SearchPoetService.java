package com.poemSys.admin.service.poetManage;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poemSys.admin.bean.Form.SearchForm;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysPoet;
import com.poemSys.common.service.SysPoetService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchPoetService
{
    @Autowired
    SysPoetService sysPoetService;

    public Result search(SearchForm searchPoetForm)
    {
        Long page = searchPoetForm.getPage();
        Long size = searchPoetForm.getSize();
        String keyword = searchPoetForm.getKeyword();

        Page<SysPoet> poetPage = new Page<>(page, size);
        if(StringUtils.isBlank(keyword))
        {
            Page<SysPoet> allPageAns = sysPoetService.page(poetPage);
            return new Result(0, "诗人信息查询成功，共"+allPageAns.getTotal()+"条数据", allPageAns);
        }
        Page<SysPoet> pageAns = sysPoetService.page(poetPage,
                new QueryWrapper<SysPoet>()
                        .like("id", keyword)
                        .or().like("name", keyword)
                        .or().like("dynasty", keyword)
                        .or().like("introduce", keyword)
        );
        return new Result(0, "诗人信息查询成功，共"+pageAns.getTotal()+"条数据", pageAns);
    }
}
