package com.poemSys.user.service.general;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poemSys.common.entity.basic.SysPost;
import com.poemSys.user.bean.PostPageAns;
import com.poemSys.user.bean.SysPostRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostPageAnsProService
{
    @Autowired
    SwapSysPostRecService swapSysPostRecService;

    public PostPageAns pro(Page<SysPost> pageAns)
    {
        //根据创建时间排序,时间相同则根据id排序
        pageAns.addOrder(OrderItem.desc("created_time"), OrderItem.asc("id"));

        List<SysPost> records = pageAns.getRecords();
        List<SysPostRes> newRecords = new ArrayList<>();
        records.forEach(r-> newRecords.add(swapSysPostRecService.swap(r)));
        return new PostPageAns(pageAns.getTotal(), pageAns.getSize(),
                pageAns.getCurrent(), pageAns.getPages(), newRecords);
    }
}
