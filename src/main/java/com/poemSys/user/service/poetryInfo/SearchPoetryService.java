package com.poemSys.user.service.poetryInfo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poemSys.admin.bean.Form.SearchForm;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysPoem;
import com.poemSys.common.entity.basic.SysPoet;
import com.poemSys.common.service.SysPoemService;
import com.poemSys.common.service.SysPoetService;
import com.poemSys.user.bean.PoemPageAns;
import com.poemSys.user.service.general.PoemPageAnsProService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class SearchPoetryService
{
    @Autowired
    SysPoemService sysPoemService;

    @Autowired
    SysPoetService sysPoetService;

    @Autowired
    PoemPageAnsProService poemPageAnsProService;

    public Result search(SearchForm searchForm)
    {
        Long page = searchForm.getPage();
        Long size = searchForm.getSize();
        String keyword = searchForm.getKeyword();

        if(StringUtils.isBlank(keyword))
            return new Result(1, "关键字不能为空", null);

        Page<SysPoem> poemPage = new Page<>(page, size);
        Page<SysPoem> poemPageAns = sysPoemService.page(poemPage,
                new QueryWrapper<SysPoem>()
                        .like("name", keyword)
                        .or().like("content", keyword)
                        .or().like("poet", keyword)
//                        .or().like("translation", keyWord)
//                        .or().like("notes", keyWord)
//                        .or().like("appreciation", keyWord)
//                        .or().like("analyse", keyWord)
//                        .or().like("background", keyWord)
                        .orderByDesc("name")
        );

        PoemPageAns poemPageAnsPro = poemPageAnsProService.pro(poemPageAns);

        Page<SysPoet> poetPage = new Page<>(page, size);
        Page<SysPoet> poetPageAns = sysPoetService.page(poetPage,
                new QueryWrapper<SysPoet>()
                        .or().like("name", keyword)
//                        .or().like("dynasty", keyWord)
//                        .or().like("introduce", keyWord)
        );


        return new Result(0, "查询成功", new res(poemPageAnsPro, poetPageAns));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class res implements Serializable
    {
        private static final long serialVersionUID = 1L;

        private PoemPageAns poemPageAns;
        private Page<SysPoet> poetPageAns;
    }
}
