package com.poemSys.user.service.general;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysPoem;
import com.poemSys.user.bean.PoemPageAns;
import com.poemSys.user.bean.SysPoemRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 对古诗词分页获取结果进行加工处理（加上isLike, isCollect字段）
 */
@Service
public class PoemPageAnsPro
{
    @Autowired
    SwapSysPoemRecService swapSysPoemRecService;

    public PoemPageAns pro(Page<SysPoem> pageAns)
    {
        List<SysPoem> records = pageAns.getRecords();
        List<SysPoemRes> newRecords = new ArrayList<>();
        records.forEach(r-> newRecords.add(swapSysPoemRecService.swap(r)));
        return new PoemPageAns(pageAns.getTotal(), pageAns.getSize(),
                pageAns.getCurrent(), pageAns.getPages(), newRecords);
    }
}
