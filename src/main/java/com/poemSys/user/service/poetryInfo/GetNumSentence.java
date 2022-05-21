package com.poemSys.user.service.poetryInfo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysSentence;
import com.poemSys.common.service.SysSentenceService;
import com.poemSys.user.bean.Form.NumForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class GetNumSentence
{
    @Autowired
    SysSentenceService sysSentenceService;

    public Result get(NumForm numForm)
    {
        int num = numForm.getNum();
        if(num>100)
            num = 100;
        int count = sysSentenceService.count();
        if(count<num*2)
            return new Result(1, "名句数量不足"+num*2+"个", null);
        List<Long> ranList = new ArrayList<>();
        while(num!=0)
        {
            long i = randomInt(1, count);
            if(!ranList.contains(i))
            {
                ranList.add(i);
                num -- ;
            }
        }
        List<SysSentence> sysSentences = sysSentenceService.list(new QueryWrapper<SysSentence>()
                .in("id", ranList));
        return new Result(0, sysSentences.size()+"条名句获取成功", sysSentences);
    }

    private static int randomInt(int min, int max){

        return new Random().nextInt(max)%(max-min+1) + min;

    }
}
