package com.poemSys.admin.service.poemManage;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poemSys.admin.bean.Form.SearchForm;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysPoem;
import com.poemSys.common.service.SysPoemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchPoemService
{
    @Autowired
    SysPoemService sysPoemService;

    public Result search(SearchForm searchPoemForm)
    {
        Long page = searchPoemForm.getPage();
        Long size = searchPoemForm.getSize();
        String keyword = searchPoemForm.getKeyword();
        Page<SysPoem> poemPage = new Page<>(page, size);
        if (StringUtils.isBlank(keyword))
        {
            Page<SysPoem> allPageAns = sysPoemService.page(poemPage);
            return new Result(0, "古诗词信息查询成功，共" + allPageAns.getTotal() + "条数据", allPageAns);
        }
        Page<SysPoem> pageAns = sysPoemService.page(poemPage,
                new QueryWrapper<SysPoem>()
                        .like("id", keyword)
                        .or().like("name", keyword)
                        .or().like("content", keyword)
                        .or().like("dynasty", keyword)
                        .or().like("poet", keyword)
                        .or().like("translation", keyword)
                        .or().like("notes", keyword)
                        .or().like("appreciation", keyword)
                        .or().like("analyse", keyword)
                        .or().like("background", keyword)
        );
        return new Result(0, "古诗词信息查询成功，共" + pageAns.getTotal() + "条数据", pageAns);
    }
}
