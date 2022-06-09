package com.poemSys.user.service.general;

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
    SwapSysPostResService swapSysPostResService;

    public PostPageAns pro(Page<SysPost> pageAns)
    {
        List<SysPost> records = pageAns.getRecords();
        List<SysPostRes> newRecords = new ArrayList<>();
        records.forEach(r-> newRecords.add(swapSysPostResService.swap(r)));
        return new PostPageAns(pageAns.getTotal(), pageAns.getSize(),
                pageAns.getCurrent(), pageAns.getPages(), newRecords);
    }
}
