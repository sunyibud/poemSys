package com.poemSys.user.service.poetryInfo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysPoet;
import com.poemSys.common.entity.connection.PoetRecommend;
import com.poemSys.common.service.PoetRecommendService;
import com.poemSys.common.service.SysPoetService;
import com.poemSys.user.bean.Form.NumForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class GetNumPoetService
{
    @Autowired
    SysPoetService sysPoetService;

    @Autowired
    PoetRecommendService poetRecommendService;

    public Result get(NumForm numForm)
    {
        int num = numForm.getNum();
        if(num>100)
            num=100;
        int count = poetRecommendService.count();
        if(count/2<=num)
            return new Result(1, "推荐诗词库总数不足"+num*2+"个", null);
        List<Long> ranList = new ArrayList<>();
        while(num!=0)
        {
            long ran = randomInt(7562, count-1);
            if(ranList.contains(ran))
                continue;
            ranList.add(ran);
            num--;
        }
        List<PoetRecommend> poetRec = poetRecommendService.list(new QueryWrapper<PoetRecommend>()
                .in("id", ranList));
        List<Long> poetIds = new ArrayList<>();
        poetRec.forEach(e->{
            poetIds.add(e.getPoetId());
        });
        List<SysPoet> list = sysPoetService.list(new QueryWrapper<SysPoet>()
                .in("id", poetIds));
        return new Result(0, list.size()+"条诗人获取成功", list);
    }


    private static int randomInt(int min, int max){

        return new Random().nextInt(max)%(max-min+1) + min;

    }
}
