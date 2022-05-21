package com.poemSys.user.service.poetryInfo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysPoem;
import com.poemSys.common.entity.connection.ConUserPoemCollect;
import com.poemSys.common.entity.connection.ConUserPoemLike;
import com.poemSys.common.entity.connection.PoemRecommend;
import com.poemSys.common.service.PoemRecommendService;
import com.poemSys.common.service.SysPoemService;
import com.poemSys.user.bean.Form.NumForm;
import com.poemSys.user.bean.SysPoemRes;
import com.poemSys.user.service.general.SwapSysPoemRecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class GetNumPoemService
{
    @Autowired
    PoemRecommendService poemRecommendService;

    @Autowired
    SysPoemService sysPoemService;

    @Autowired
    SwapSysPoemRecService swapSysPoemRecService;

    public Result get(NumForm numForm)
    {
        int num = numForm.getNum();
        if(num>100)
            num=100;
        int count = poemRecommendService.count();
        if(count/2<=num)
            return new Result(1, "推荐诗词库总数不足"+num*2+"个", null);
        List<Long> ranList = new ArrayList<>();
        while(num!=0)
        {
            long ran = randomInt(1, count);
            if(ranList.contains(ran))
                continue;
            ranList.add(ran);
            num--;
        }
        List<PoemRecommend> poemRec = poemRecommendService.list(new QueryWrapper<PoemRecommend>()
                .in("id", ranList));
        List<Long> poemIds = new ArrayList<>();
        poemRec.forEach(e->{
            poemIds.add(e.getPoemId());
        });
        List<SysPoem> list = sysPoemService.list(new QueryWrapper<SysPoem>()
                .in("id", poemIds));
        List<SysPoemRes> newList = new ArrayList<>();
        list.forEach(l->{
            newList.add(swapSysPoemRecService.swap(l));
        });
        return new Result(0, list.size()+"条古诗词获取成功", newList);
    }


    private static int randomInt(int min, int max){

        return new Random().nextInt(max)%(max-min+1) + min;

    }
}
