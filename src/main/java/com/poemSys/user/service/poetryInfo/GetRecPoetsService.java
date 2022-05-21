package com.poemSys.user.service.poetryInfo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysPoet;
import com.poemSys.common.entity.connection.PoetRecommend;
import com.poemSys.common.service.PoetRecommendService;
import com.poemSys.common.service.SysPoetService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取100个推荐诗人
 */
@Service
public class GetRecPoetsService
{
    @Autowired
    PoetRecommendService poetRecommendService;

    @Autowired
    SysPoetService sysPoetService;

    public Result get()
    {
        List<Long> list = new ArrayList<>();
        for(long i=7562; i<=7562+100; i++)
            list.add(i);
        List<PoetRecommend> poetRec = poetRecommendService.list(new QueryWrapper<PoetRecommend>()
                .in("id", list));
        List<Long> poetIds = new ArrayList<>();
        poetRec.forEach(e->{
            poetIds.add(e.getPoetId());
        });
        List<SysPoet> poetList = sysPoetService.list(new QueryWrapper<SysPoet>()
                .in("id", poetIds));
        List<PoetIdName> resList = new ArrayList<>();
        poetList.forEach(e->{
            resList.add(new PoetIdName(e.getId(), e.getName()));
        });
        return new Result(0, "推荐诗人列表获取成功", resList);
    }
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    private static class PoetIdName implements Serializable
    {
        private static final long serialVersionUID = 1L;

        private long id;
        private String name;
    }
}
