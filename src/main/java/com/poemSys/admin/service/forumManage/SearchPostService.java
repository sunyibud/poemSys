package com.poemSys.admin.service.forumManage;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poemSys.admin.bean.Form.SearchForm;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysPost;
import com.poemSys.common.service.SysPostService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchPostService
{
    @Autowired
    SysPostService sysPostService;

    public Result search(SearchForm searchForm)
    {
        Long page = searchForm.getPage();
        Long size = searchForm.getSize();
        String keyword = searchForm.getKeyword();

        Page<SysPost> postPage = new Page<>(page, size);

        if(StringUtils.isBlank(keyword))
        {
            Page<SysPost> allPageAns = sysPostService.page(postPage);
            return new Result(0, "帖子信息查询成功，共"+allPageAns.getTotal()+"条数据", allPageAns);
        }
        Page<SysPost> pageAns = sysPostService.page(postPage,
                new QueryWrapper<SysPost>()
                        .like("id", keyword)
                        .or().like("title", keyword)
                        .or().like("content", keyword)
        );
        return new Result(0, "帖子信息查询成功，共"+pageAns.getTotal()+"条数据", pageAns);
    }
}
